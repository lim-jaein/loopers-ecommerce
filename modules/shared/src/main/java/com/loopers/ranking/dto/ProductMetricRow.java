package com.loopers.ranking.dto;

public record ProductMetricRow(
        Long productId,
        long likeCount,
        long salesCount,
        long salesAmount,
        long viewCount
) {
    public static ProductMetricRow of(Long productId, long likeCount, long salesCount, long salesAmount, long viewCount) {
        return new ProductMetricRow(
                productId, likeCount, salesCount, salesAmount, viewCount
        );
    }
}
