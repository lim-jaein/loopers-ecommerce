package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;

import java.math.BigDecimal;

public record ProductDetailInfo(
        Long productId,
        String productName,
        Long brandId,
        String brandName,
        BigDecimal price,
        int stock,
        int likeCount
) {
    public static ProductDetailInfo of(Product product, Brand brand) {
        return new ProductDetailInfo(
                product.getId(),
                product.getName(),
                brand.getId(),
                brand.getName(),
                product.getPrice().getAmount(),
                product.getStock().getQuantity(),
                product.getLikeCount()
        );
    }
}
