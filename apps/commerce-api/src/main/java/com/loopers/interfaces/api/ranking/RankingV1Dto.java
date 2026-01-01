package com.loopers.interfaces.api.ranking;

import com.loopers.application.product.ProductInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RankingV1Dto {

    public record RankingProductResponse(
            Long productId,
            String productName,
            Long brandId,
            BigDecimal price,
            int likeCount,
            Long rank,  // 랭킹 순위 추가
            LocalDateTime createdAt
    ) {
        public static RankingProductResponse of(ProductInfo info, Long rank) {
            return new RankingProductResponse(
                    info.id(),
                    info.name(),
                    info.brandId(),
                    info.priceAmount(),
                    info.likeCount(),
                    rank,
                    info.createdAt()
            );
        }
    }
}
