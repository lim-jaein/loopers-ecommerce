package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
         WHERE (:brandId IS NULL OR p.brandId = :brandId)
    """)
    Page<Product> findProducts(Long brandId, Pageable pageRequest);
}
