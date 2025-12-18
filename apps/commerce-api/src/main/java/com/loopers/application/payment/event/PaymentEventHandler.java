package com.loopers.application.payment.event;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.event.PaymentRequested;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.application.payment.PaymentProcessService;
import com.loopers.domain.common.event.DomainEvent;
import com.loopers.domain.common.event.DomainEventEnvelop;
import com.loopers.domain.common.event.DomainEventRepository;
import com.loopers.support.json.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventHandler {

    private final OrderFacade orderFacade;
    private final PaymentFacade paymentFacade;
    private final PaymentProcessService paymentProcessService;

    private final ApplicationEventPublisher eventPublisher;
    private final DomainEventRepository eventRepository;
    private final JsonConverter jsonConverter;

    /**
     * 외부 PG 결제 요청 시,
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    void handleExternalPgPayment(PaymentRequested event) {
        paymentProcessService.processPg(event.userId(), event.orderId());
    }


    /**
     * 결제 성공 시, 트랜잭션 커밋 이전 상태 변경 및 outbox 테이블에 이벤트를 저장합니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOutboxEvent(PaymentSucceededEvent event) {

        DomainEventEnvelop<PaymentSucceededEvent> envelop =
                DomainEventEnvelop.of(
                        "order-events",
                        "ORDER",
                        event.orderId(),
                        event
                );

        eventRepository.save(
                DomainEvent.pending(
                        envelop,
                        jsonConverter.serialize(envelop)
                )
        );
    }
}
