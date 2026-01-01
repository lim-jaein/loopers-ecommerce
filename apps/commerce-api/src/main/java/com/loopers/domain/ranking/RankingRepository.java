package com.loopers.domain.ranking;

import com.loopers.ranking.streamer.RankingInfo;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface RankingRepository {
    List<RankingInfo> findRankings(LocalDate date, Pageable pageable);
    long count(LocalDate date);
    Long findRank(LocalDate date, Long productId);
}
