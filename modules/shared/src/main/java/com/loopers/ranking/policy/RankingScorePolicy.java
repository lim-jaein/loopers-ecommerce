package com.loopers.ranking.policy;

public interface RankingScorePolicy {

    double caculateRankingScore(
            long likeCount,
            long salesAmount,
            long viewCount
    );

    double getEventScore(
            String eventName,
            long value
    );
}
