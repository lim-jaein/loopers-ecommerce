package com.loopers.batch.job.ranking.reader;

import com.loopers.ranking.dto.ProductMetricRow;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMetricsReader {
    private final EntityManagerFactory entityManagerFactory;

    private static final String SELECT_SQL = """
        SELECT product_id, SUM(like_count), SUM(sales_amount), SUM(view_count)
          FROM product_metrics
         WHERE metric_date BETWEEN :fromDate AND :toDate
         GROUP BY product_id
         LIMIT :limit OFFSET :offset
    """;

    private static final String SELECT_PRODUCT_ID_SQL = """
        SELECT product_id, SUM(like_count), SUM(sales_amount), SUM(view_count)
          FROM product_metrics
         WHERE metric_date BETWEEN :fromDate AND :toDate
           AND product_id IN (:productIds)
         GROUP BY product_id
    """;

    public List<ProductMetricRow> readPage(
            String fromDate,
            String toDate,
            int page,
            int size
    ) {

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            @SuppressWarnings("unchecked")
            List<Object[]> rawRows = entityManager.createNativeQuery(SELECT_SQL)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("limit", size)
                    .setParameter("offset", page * size)
                    .getResultList();

            return rawRows.stream()
                    .map(r -> new ProductMetricRow(
                            ((Number) r[0]).longValue(),
                            ((Number) r[1]).longValue(),
                            ((Number) r[2]).longValue(),
                            ((Number) r[3]).longValue(),
                            ((Number) r[4]).longValue()
                    ))
                    .toList();
        }
    }

    public List<ProductMetricRow> readByProductIds(
            String fromDate,
            String toDate,
            List<Long> productIds
    ) {
        if(productIds.isEmpty()) return List.of();

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            @SuppressWarnings("unchecked")
            List<Object[]> rawRows =
                    entityManager.createNativeQuery(SELECT_PRODUCT_ID_SQL)
                            .setParameter("fromDate", fromDate)
                            .setParameter("toDate", toDate)
                            .setParameter("productIds", productIds)
                            .getResultList();

            return rawRows.stream()
                    .map(r -> new ProductMetricRow(
                            ((Number) r[0]).longValue(),
                            ((Number) r[1]).longValue(),
                            ((Number) r[2]).longValue(),
                            ((Number) r[3]).longValue(),
                            ((Number) r[4]).longValue()
                    ))
                    .toList();
        }
    }
}
