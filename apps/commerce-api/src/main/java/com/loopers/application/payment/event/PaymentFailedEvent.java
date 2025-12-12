package com.loopers.application.payment.event;

import java.time.Instant;

public record PaymentFailedEvent(
        Long orderId,
        String msg,
        Instant ocurredAt
) {
    public static PaymentFailedEvent of(Long orderId, Exception e) {
        return new PaymentFailedEvent(
                orderId,
                e.getMessage(),
                Instant.now()
        );
    }
}
