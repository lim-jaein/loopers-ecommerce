package com.loopers.application.payment;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointService;
import com.loopers.infrastructure.ResilientPgClient;
import com.loopers.interfaces.api.payment.PaymentV1Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final PointService pointService;
    private final ResilientPgClient resilientPgClient;
    private final OrderService orderService;

    @Transactional
    public void pay(Order order, PaymentV1Dto.PaymentRequest request) {
       switch (request) {
           case PaymentV1Dto.CardPaymentInfo cardInfo -> {
               PaymentV1Dto.PgPaymentResponse response = resilientPgClient.requestPayment(PaymentV1Dto.PgPaymentRequest.of(order, cardInfo));
               orderService.saveTransactionKey(order.getId(), response.transactionKey());
           }
           case PaymentV1Dto.PointPaymentInfo pointInfo -> {
               Point point = pointService.findPointWithLock(order.getUserId()).orElseThrow();
               point.use(order.calculateTotalPrice());
           }
       }
    }
}
