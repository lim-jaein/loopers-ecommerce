package com.loopers.application.order.event;

import com.loopers.interfaces.api.payment.PaymentMethod;

import java.time.Instant;

public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        Instant ocurredAt
) {
    public static OrderCreatedEvent of(Long orderId, Long userId, PaymentMethod method) {
        return new OrderCreatedEvent(
                orderId,
                userId,
                Instant.now()
        );
    }
}
