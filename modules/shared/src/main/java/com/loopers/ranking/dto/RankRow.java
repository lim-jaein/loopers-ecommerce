package com.loopers.ranking.dto;

public record RankRow(
        Long productId,
        int rank
) {
    public static RankRow of(Long productId, int rank) {
        return new RankRow(
                productId, rank
        );
    }
}
