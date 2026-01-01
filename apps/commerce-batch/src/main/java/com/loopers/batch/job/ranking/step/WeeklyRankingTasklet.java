package com.loopers.batch.job.ranking.step;

import com.loopers.batch.job.ranking.RankingJobConfig;
import com.loopers.batch.job.ranking.reader.ProductMetricsReader;
import com.loopers.cache.CacheKeyService;
import com.loopers.ranking.domain.ProductMetricsWeekly;
import com.loopers.ranking.dto.ProductMetricRow;
import com.loopers.ranking.dto.RankRow;
import com.loopers.ranking.policy.RankingScorePolicy;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@StepScope
@ConditionalOnProperty(name = "spring.batch.job.name", havingValue = RankingJobConfig.JOB_NAME)
@RequiredArgsConstructor
@Component
public class WeeklyRankingTasklet implements Tasklet {

    private final ProductMetricsReader productMetricsReader;
    private final EntityManagerFactory entityManagerFactory;

    private final RankingScorePolicy rankingScorePolicy;
    private final RedisTemplate<String, String> redisTemplate;
    private final CacheKeyService cacheKeyService;

    private static final int PAGE_SIZE = 1000;

    @Value("#{jobParameters['requestDate']}")
    private String requestDate;

    private String fromDate;

    private String toDate;

    private static final String DELETE_SQL = """
                DELETE FROM product_metrics_weekly
            """;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        if (requestDate == null) {
            throw new RuntimeException("실행일자가 null 입니다.");
        }

        fromDate = LocalDate.parse(requestDate).minusDays(29).toString();
        toDate = requestDate;

        log.info("WeeklyRanking Tasklet 실행: {} ~ {}, 실행 요청 일자 : {}", fromDate, toDate, requestDate);

        int page = 0;
        String snapshotKey = cacheKeyService.rankingWeeklySnapshotKey(requestDate);
        String latestKey = cacheKeyService.rankingWeeklyLastestKey();

        // 재실행 시 Redis 키 삭제
        redisTemplate.delete(snapshotKey);

        // chunk oriented programming
        while (true) {
            // 1. Reader: PAGE_SIZE별 일간 집계 조회
            List<ProductMetricRow> rows = productMetricsReader.readPage(fromDate, toDate, page, PAGE_SIZE);

            if (rows.isEmpty()) break;

            // 2. Processor: PAGE_SIZE별 랭킹스코어 계산 및 Redis ZSet 적재
            for (ProductMetricRow row : rows) {
                // 랭킹 스코어 계산
                double score = rankingScorePolicy.caculateRankingScore(
                        row.likeCount(),
                        row.salesAmount(),
                        row.viewCount()
                );

                // Redis ZSet 저장
                redisTemplate.opsForZSet()
                        .add(
                                snapshotKey,
                                row.productId().toString(),
                                score
                        );
            }
            page++;
        }

        // 3. Writer: Redis Top100 조회 후 mv 적재
        List<RankRow> rankRows = readTop100Ranks(snapshotKey);

        if (!rankRows.isEmpty()) {
            applySnapShot(rankRows);

            // API 속도 개선을 위한 Redis 설정
            redisTemplate.rename(snapshotKey, latestKey);

            // 집계에 오류가 있더라도 캐시로 조회가능하도록 함
            redisTemplate.expire(latestKey, Duration.ofDays(2));
        }

        log.info("WeeklyRanking Tasklet 작업 완료");
        return RepeatStatus.FINISHED;
    }

    /**
     * Redis ZSet에서 주간 Top100 랭킹 데이터를 읽어온다.
     * @param snapshotKey
     * @return
     */
    private List<RankRow> readTop100Ranks(String snapshotKey) {
        Set<ZSetOperations.TypedTuple<String>> top100 =
                redisTemplate.opsForZSet()
                        .reverseRangeWithScores(snapshotKey, 0, 99);


        if (top100 == null || top100.isEmpty()) {
            return List.of();
        }

        int rank = 1;
        List<RankRow> result = new ArrayList<>(top100.size());
        for (ZSetOperations.TypedTuple<String> tuple : top100) {
            result.add(
                    RankRow.of(
                            Long.valueOf(tuple.getValue()),
                            rank++
                    )
            );
        }

        return result;
    }

    /**
     * 랭킹 상품의 정보를 읽어와 mv에 적재한다
     * @param top100
     */
    private void applySnapShot(List<RankRow> top100) {
        if (top100 == null || top100.isEmpty()) {
            return;
        }

        // 1. Top100 productIds 추출
        List<Long> productIds = top100.stream()
                .map(RankRow::productId)
                .toList();

        // 2. Top100 상품에 대한 집계 정보 재조회
        List<ProductMetricRow> rows =
                productMetricsReader.readByProductIds(fromDate, toDate, productIds);

        // 3. 랭킹정보 매핑을 위한 HashMap 생성
        Map<Long, Integer> rankMap = new HashMap<>();
        for (RankRow row : top100) {
            rankMap.put(row.productId(), row.rank());
        }

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        try (StatelessSession session = sessionFactory.openStatelessSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                // 기존 mv 전체 삭제
                session.createNativeMutationQuery(DELETE_SQL).executeUpdate();

                // mv 저장
                for (ProductMetricRow row : rows) {
                    Integer rank = rankMap.get(row.productId());

                    session.insert(
                            ProductMetricsWeekly.of(
                                    row,
                                    rank
                            )
                    );
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }
}
