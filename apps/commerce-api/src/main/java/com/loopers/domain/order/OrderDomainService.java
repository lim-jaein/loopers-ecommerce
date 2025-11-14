package com.loopers.domain.order;

import com.loopers.domain.common.vo.Money;
import com.loopers.domain.point.Point;
import com.loopers.domain.product.Product;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderDomainService {

    public void processPaymentAndInventory(Order order, Map<Long, Product> productsById, Point point) {

        Money totalAmount = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Money.zero(), Money::plus);

        // 주문 상품별 재고 차감
        for (OrderItem item : order.getItems()) {
            Product product = productsById.get(item.getProductId());
            product.deductStock(item.getQuantity());
        }

        // 결제 : 유저 포인트 차감
        point.use(totalAmount);

        // 결제 완료 (PAID) : 주문 상태 변경
        order.changeToPaid();
    }
}
