package com.loopers.domain.product.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StockTest {

    @DisplayName("재고를 저장할 때, ")
    @Nested
    class Save {

        @DisplayName("재고가 0 이면 정상 저장된다.")
        @Test
        void succeeds_whenStockIsZero() {
            // arrange + act
            Stock stock = new Stock(0);

            // assert
            assertThat(stock.getQuantity()).isEqualTo(0);
        }

        @DisplayName("재고가 1 이상이면 정상 저장된다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 100})
        void succeeds_whenStockIsPositive(int validStock) {
            // arrange + act
            Stock stock = new Stock(validStock);

            // assert
            assertThat(stock.getQuantity()).isEqualTo(validStock);
        }

        @DisplayName("재고가 0 보다 작으면 등록 실패한다.")
        @Test
        void fails_whenStockIsLessThanZero() {
            // arrange + act + assert
            assertThatThrownBy(() -> new Stock(-100))
                    .hasMessageContaining("재고는 0보다 작을 수 없습니다.");

        }
    }

    @DisplayName("재고를 차감할 때, ")
    @Nested
    class Use {

        @DisplayName("재고 차감 수량이 1 이상이면 정상 차감된다.")
        @Test
        void succeeds_whenStockIsPositive() {
            // arrange
            Stock stock = new Stock(10);

            // act
            Stock decreasedStock = stock.decrease(5);

            // assert
            assertThat(decreasedStock.getQuantity()).isEqualTo(5);
        }

        @DisplayName("재고 차감 수량이 0이면 변동 없다.")
        @Test
        void succeeds_whenStockIsZero() {
            // arrange
            Stock stock = new Stock(10);

            // act
            Stock decreasedStock = stock.decrease(0);

            // assert
            assertThat(decreasedStock.getQuantity()).isEqualTo(10);
        }

        @DisplayName("재고 차감 수량이 재고보다 크면 실패한다.")
        @Test
        void fails_whenNotEnoughStock() {
            // arrange
            Stock stock = new Stock(10);

            // act + assert
            assertThatThrownBy(() -> stock.decrease(11))
                    .hasMessageContaining("재고가 부족합니다.");

        }
    }
}
