package com.loopers.ranking.streamer;

public record RankingInfo(
        Long id,
        Long rank
) {
    public static RankingInfo of(Long id, Long rank) {
        return new RankingInfo(
                id,
                rank
        );
    }
}
