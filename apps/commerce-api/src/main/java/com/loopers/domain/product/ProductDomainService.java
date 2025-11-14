package com.loopers.domain.product;

import com.loopers.application.product.ProductDetailInfo;
import com.loopers.domain.brand.Brand;
import org.springframework.stereotype.Service;

@Service
public class ProductDomainService {
    public ProductDetailInfo createProductDetail(Product product, Brand brand) {
        return ProductDetailInfo.of(product, brand);
    }
}
