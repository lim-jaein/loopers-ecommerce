package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointTest {
    @DisplayName("포인트를 충전할 때, ")
    @Nested
    class Charge {
        @DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1000})
        void chargePoint_failsWhenAmountIsZeroOrNegative(int invalidAmount) {
            // arrange
            Point point = Point.create(1L, 0);

            // act + assert
            CoreException result = assertThrows(CoreException.class, () ->
                    point.increase(invalidAmount)
            );

            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("충전금액이 1 이상일 때, 포인트가 정상 충전된다.")
        @Test
        void chargePoint() {
            // arrange
            Point point = Point.create(1L, 0);
            int chargeAmount = 1000;

            // act
            point.increase(chargeAmount);

            // assert
            assertThat(point.getBalance()).isEqualTo(chargeAmount);
        }
    }
}
