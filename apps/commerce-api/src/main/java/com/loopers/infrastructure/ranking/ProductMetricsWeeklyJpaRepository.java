package com.loopers.infrastructure.ranking;

import com.loopers.ranking.domain.ProductMetricsWeekly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMetricsWeeklyJpaRepository extends JpaRepository<ProductMetricsWeekly, Long> {
    Page<ProductMetricsWeekly> findAllByOrderByRankNoAsc(Pageable pageable);
}
