package com.loopers.domain.product.vo;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Stock {
    private int quantity;

    public Stock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("재고는 0보다 작을 수 없습니다.");
        }
        this.quantity = quantity;
    }

    protected Stock() {}

    public static Stock of(int quantity) {
        return new Stock(quantity);
    }

    public Stock decrease(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("주문 상품의 재고가 부족합니다.");
        }
        return new Stock(this.quantity - quantity);
    }
}
