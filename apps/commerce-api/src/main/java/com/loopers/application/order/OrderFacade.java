package com.loopers.application.order;

import com.loopers.domain.common.vo.Money;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderDomainService;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OrderFacade {
    private final OrderService orderService;
    private final PointService pointService;
    private final OrderDomainService orderDomainService;
    private final ProductService productService;

    private final OrderExternalSystemSender orderExternalSystemSender;

    @Transactional
    public Order createOrder(Long userId, List<OrderItemInfo> itemInfoList) {

        // 주문 생성
        Order order = Order.create(userId);

        // 주문 아이템 세팅
        for (OrderItemInfo info : itemInfoList) {
            Product p = productService.getProduct(info.productId());

            order.addItem(
                    info.productId(),
                    info.quantity(),
                    Money.of(p.getPrice().getAmount()),
                    Money.of(p.getPrice().getAmount()).multiply(info.quantity())
            );
        }

        // 주문 저장
        orderService.createOrder(order);

        // 포인트 조회
        Point point = pointService.findPoint(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 유저입니다."));

        // 상품ID 추출
        List<Long> productIds = itemInfoList.stream()
                .map(OrderItemInfo::productId)
                .toList();

        // 상품정보 조회
        List<Product> products = productService.findAll(productIds);

        Map<Long, Product> productsById = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        // 주문처리 도메인서비스 호출
        orderDomainService.processPaymentAndInventory(order, productsById, point);

        // 주문 정보 외부 시스템 전송
        orderExternalSystemSender.send(order);

        return order;
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long userId, Long orderId) {
        Order order = orderService.findOrder(userId, orderId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문입니다."));
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> getOrders(Long userId) {
        List<Order> orders = orderService.findAll(userId);
        return orders;
    }
}
