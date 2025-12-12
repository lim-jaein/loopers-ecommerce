package com.loopers.application.like.event;

import java.time.Instant;

public record LikeCreatedEvent(
        Long productId,
        Instant ocurredAt
) {
    public static LikeCreatedEvent from(Long productId) {
        return new LikeCreatedEvent(
                productId,
                Instant.now()
        );
    }
}
