package com.loopers.ranking.policy;

import org.springframework.stereotype.Component;

@Component
public class DefaultRankingScorePolicy implements RankingScorePolicy {

    private static final double RANKING_VIEW_WEIGHT = 0.1;
    private static final double RANKING_LIKE_WEIGHT = 0.2;
    private static final double RANKING_ORDER_WEIGHT = 0.7;


    @Override
    public double calculateRankingScore(long likeCount, long salesAmount, long viewCount) {
        return RANKING_LIKE_WEIGHT * likeCount +
                RANKING_ORDER_WEIGHT * Math.log1p(salesAmount) +
                RANKING_VIEW_WEIGHT * viewCount;
    }

    @Override
    public double getEventScore(String eventName, long value) {
        return switch (eventName) {
            case "ORDER_PAID" -> calculateRankingScore(0, value, 0);
            case "LIKE_CREATED" -> calculateRankingScore(value, 0, 0);
            case "PRODUCT_VIEWED" -> calculateRankingScore(0, 0, value);
            default -> throw new IllegalStateException("알 수 없는 이벤트명 입니다. " + eventName);
        };
    }
}
