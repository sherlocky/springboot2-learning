package com.sherlocky.springboot2.rocketmq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Service;

/**
 * The consumer that replying String
 */
@Service
@RocketMQMessageListener(topic = "${sherlocky.rocketmq.stringRequestTopic}", consumerGroup = "${sherlocky.rocketmq.stringRequestConsumer}", selectorExpression = "${sherlocky.rocketmq.tag}")
public class StringConsumerWithReplyString implements RocketMQReplyListener<String, String> {

    @Override
    public String onMessage(String message) {
        System.out.printf("------- StringConsumerWithReplyString received: %s \n", message);
        return "reply string";
    }
}
