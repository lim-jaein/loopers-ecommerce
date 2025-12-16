package com.loopers.application.payment.event;

import com.loopers.application.order.OrderExternalSystemSender;
import com.loopers.application.payment.PaymentFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventHandler {
    private final PaymentFacade paymentFacade;
    private final OrderExternalSystemSender orderExternalSystemSender;

    /**
     * 결제 성공 시, 상태 변경 후 외부 시스템으로 주문 / 결제 정보를 전송합니다.
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    void handleOrderCreatedExternalSend(PaymentSucceededEvent event) {
        try {
            orderExternalSystemSender.send(event.orderId());
        } catch (Exception e) {
            log.error("외부 시스템으로의 주문 전송 실패, 주문 ID: {}", event.orderId(), e);
        }
    }
}
