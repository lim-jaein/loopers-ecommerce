package com.loopers.ranking.domain;

import com.loopers.ranking.dto.ProductMetricRow;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "product_metrics_monthly")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductMetricsMonthly {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "like_count")
    private long likeCount;

    @Column(name = "order_count")
    private long orderCount;

    @Column(name = "view_count")
    private long viewCount;

    @Column(name = "rank_no")
    private Integer rankNo;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static ProductMetricsMonthly of(ProductMetricRow row, Integer rank) {
        return new ProductMetricsMonthly(
                row.productId(),
                row.likeCount(),
                row.salesCount(),
                row.viewCount(),
                rank,
                LocalDateTime.now()
        );
    }
}
