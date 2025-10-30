package com.loopers.domain.point;

import java.util.Optional;

public interface PointService {
    Optional<Integer> findPoint(Long userId);

    void chargePoint(Long userId, int amount);
}
