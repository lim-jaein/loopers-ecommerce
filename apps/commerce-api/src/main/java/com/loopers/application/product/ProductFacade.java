package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDomainService;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductFacade {
    private final ProductService productService;
    private final BrandService brandService;
    private final ProductDomainService productDomainService;

    public Page<ProductInfo> getProducts(Long brandId, Pageable pageable, String sort) {
        Page<Product> products = productService.getProducts(brandId, pageable, sort);
        return products.map(ProductInfo::from);
    }

    public ProductDetailInfo getProductDetail(Long productId) {
        Product p = productService.getProduct(productId);
        Brand b = brandService.getBrand(p.getBrandId());
        return productDomainService.createProductDetail(p, b);
    }
}
