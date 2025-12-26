package com.loopers.domain.productmetrics;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductMetricsRepository {

    Optional<ProductMetrics> findByProductId(Long productId);

    void upsertLikeCount(Long productId);

    void upsertUnlikeCount(Long productId);

    void upsertSalesCount(Long productId, int quantity, BigDecimal amount);

    void upsertViewCount(Long productId);
}
