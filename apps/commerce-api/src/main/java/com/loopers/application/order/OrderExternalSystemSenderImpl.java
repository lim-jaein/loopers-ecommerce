package com.loopers.application.order;

import com.loopers.domain.order.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderExternalSystemSenderImpl implements OrderExternalSystemSender{
    @Override
    public void send(Order order) {

    }
}
