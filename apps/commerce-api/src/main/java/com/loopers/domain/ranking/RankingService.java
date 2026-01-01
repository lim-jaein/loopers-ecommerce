package com.loopers.domain.ranking;

import com.loopers.ranking.streamer.RankingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository dailyRankingRepository;
    private final RankingRepository weeklyRankingRepository;
    private final RankingRepository monthlyRankingRepository;

    public List<RankingInfo> getRankings(String period, LocalDate rankingDate, Pageable pageable) {
        RankingRepository repository = getRepository(period);
        return repository.findRankings(rankingDate, pageable);
    }

    public long getTotalCount(String period, LocalDate rankingDate) {
        RankingRepository repository = getRepository(period);
        return repository.count(rankingDate);
    }

    private RankingRepository getRepository(String period) {
        if ("weekly".equalsIgnoreCase(period)) {
            return weeklyRankingRepository;
        } else if ("monthly".equalsIgnoreCase(period)) {
            return monthlyRankingRepository;
        }
        return dailyRankingRepository;
    }

    public Long getRanking(LocalDate rankingDate, long productId) {
        return dailyRankingRepository.findRank(rankingDate, productId);
    }
}
