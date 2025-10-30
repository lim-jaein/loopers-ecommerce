package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "points")
public class Point {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int balance;

    protected Point() {
    }

    private Point(Long userId) {
        this.userId = userId;
        this.balance = 0;
    }

    public static Point create(Long userId) {
        return new Point(userId);
    }

    public void increase(int amount) {
        if (amount <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "충전 금액은 0보다 커야 합니다.");
        }

        this.balance += amount;
        return;
    }
}
