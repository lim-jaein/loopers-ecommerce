package com.loopers.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findByIdAndUserId(Long id, Long userId);

    Optional<Order> findById(Long id);

    Order save(Order order);

    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime threshold);
}
