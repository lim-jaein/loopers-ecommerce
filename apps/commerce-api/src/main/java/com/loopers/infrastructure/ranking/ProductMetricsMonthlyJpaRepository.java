package com.loopers.infrastructure.ranking;

import com.loopers.ranking.domain.ProductMetricsMonthly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMetricsMonthlyJpaRepository extends JpaRepository<ProductMetricsMonthly, Long> {
    Page<ProductMetricsMonthly> findAllByOrderByRankNoAsc(Pageable pageable);
}
