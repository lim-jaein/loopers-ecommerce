package com.loopers.domain.ranking;

import com.loopers.ranking.streamer.RankingInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MonthlyRankingRepository {
    List<RankingInfo> findAll(Pageable pageable);
    long count();
}
