package com.loopers.interfaces.api.ranking.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingResponse {
    private Long productId;
    private Integer rank;
    private Long likeCount;
    private Long salesAmount;
    private Long viewCount;
}
