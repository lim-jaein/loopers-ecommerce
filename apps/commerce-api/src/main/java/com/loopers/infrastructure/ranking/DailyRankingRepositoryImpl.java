package com.loopers.infrastructure.ranking;

import com.loopers.cache.CacheKeyService;
import com.loopers.domain.ranking.RankingRepository;
import com.loopers.ranking.streamer.RankingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Repository("dailyRankingRepository")
@RequiredArgsConstructor
public class DailyRankingRepositoryImpl implements RankingRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final CacheKeyService cacheKeyService;

    @Override
    public List<RankingInfo> findRankings(LocalDate date, Pageable pageable) {
        return findPage(date, pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public long count(LocalDate date) {
        return findTotalCount(date);
    }

    @Override
    public Long findRank(LocalDate date, Long productId) {
        return redisTemplate.opsForZSet()
                .reverseRank(
                        cacheKeyService.rankingKey(date),
                        productId.toString()
                );
    }

    /**
     * 랭킹 페이지 Top-N 조회 (내부 메서드)
     */
    private List<RankingInfo> findPage(
            LocalDate date,
            int page,
            int size
    ) {
        long start = (long) (page) * size; // Pageable은 0부터 시작
        long end = start + size - 1;
        long offset = (long) (page) * size;

        Set<ZSetOperations.TypedTuple<String>> zset = redisTemplate.opsForZSet().reverseRangeWithScores(
                cacheKeyService.rankingKey(date),
                start,
                end
        );

        if (zset == null || zset.isEmpty()) {
            return List.of();
        }

        List<ZSetOperations.TypedTuple<String>> zlist = List.copyOf(zset);

        return IntStream.range(0, zset.size())
                .mapToObj(i -> {
                    ZSetOperations.TypedTuple<String> s = zlist.get(i);
                    return new RankingInfo(
                            Long.valueOf(s.getValue()),
                            offset + i + 1
                    );
                })
                .toList();
    }

    private long findTotalCount(LocalDate rankingDate) {
        Long count = redisTemplate.opsForZSet().zCard(cacheKeyService.rankingKey(rankingDate));
        return count != null ? count : 0;
    }
}
