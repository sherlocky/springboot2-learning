package com.sherlocky.springboot2.rocketmq.consumer;

import com.sherlocky.springboot2.rocketmq.domain.OrderPaidEvent;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * OrderPaidEventConsumer
 */
@Service
@RocketMQMessageListener(topic = "${sherlocky.rocketmq.orderTopic}", consumerGroup = "order-paid-consumer")
public class OrderPaidEventConsumer implements RocketMQListener<OrderPaidEvent> {

    @Override
    public void onMessage(OrderPaidEvent orderPaidEvent) {
        System.out.printf("------- OrderPaidEventConsumer received: %s [orderId : %s]\n", orderPaidEvent,orderPaidEvent.getOrderId());
    }
}
