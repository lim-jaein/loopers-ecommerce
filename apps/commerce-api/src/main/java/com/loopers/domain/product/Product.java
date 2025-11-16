package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.common.vo.Money;
import com.loopers.domain.product.vo.Stock;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_id", nullable = false)
    private Long brandId;
    @Column(nullable = false)
    private String name;
    @Embedded
    private Money price;
    @Embedded
    private Stock stock;
    private int likeCount;

    protected Product() {}

    public Product(Long brandId, String name, Money price, Stock stock) {
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("브랜드 ID는 음수 혹은 null일 수 없습니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명은 비어있을 수 없습니다.");
        }
        if (price == null) {
            throw new IllegalArgumentException("상품가격은 비어있을 수 없습니다.");
        }
        if (stock == null) {
            throw new IllegalArgumentException("재고는 비어있을 수 없습니다.");
        }
        this.brandId = brandId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.likeCount = 0;
    }

    public static Product create(Long brandId, String name, Money price, Stock stock) {
        return new Product(brandId, name, price, stock);
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        if (this.likeCount <= 0) {
            throw new IllegalStateException("좋아요 수는 0 미만일 수 없습니다.");
        }
        this.likeCount -= 1;
    }

    public void deductStock(int deductQty) {
        this.stock = this.stock.decrease(deductQty);
    }

}
