package com.loopers.application.order.event;

import java.time.Instant;

public record PaymentRequested(
        Long orderId,
        Long userId,
        Instant occurredAt
) {
    public static PaymentRequested of(Long orderId, Long userId) {
        return new PaymentRequested(
                orderId,
                userId,
                Instant.now()
        );
    }
}
