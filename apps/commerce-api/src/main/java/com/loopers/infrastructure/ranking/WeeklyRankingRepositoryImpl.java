package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.RankingRepository;
import com.loopers.ranking.streamer.RankingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository("weeklyRankingRepository")
@RequiredArgsConstructor
public class WeeklyRankingRepositoryImpl implements RankingRepository {
    private final ProductMetricsWeeklyJpaRepository jpaRepository;

    @Override
    public List<RankingInfo> findRankings(LocalDate date, Pageable pageable) {
        return jpaRepository.findAllByOrderByRankNoAsc(pageable)
                .map(m -> new RankingInfo(m.getProductId(), m.getRankNo().longValue()))
                .getContent();
    }

    @Override
    public long count(LocalDate date) {
        return jpaRepository.count();
    }

    @Override
    public Long findRank(LocalDate date, Long productId) {
        return jpaRepository.findById(productId)
                .map(m -> m.getRankNo().longValue())
                .orElse(null);
    }
}
