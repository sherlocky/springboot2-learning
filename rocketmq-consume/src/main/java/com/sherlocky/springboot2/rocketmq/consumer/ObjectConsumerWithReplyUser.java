package com.sherlocky.springboot2.rocketmq.consumer;

import com.sherlocky.springboot2.rocketmq.domain.User;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Service;

/**
 * The consumer that replying Object
 */
@Service
@RocketMQMessageListener(topic = "${sherlocky.rocketmq.objectRequestTopic}", consumerGroup = "${sherlocky.rocketmq.objectRequestConsumer}", selectorExpression = "${sherlocky.rocketmq.tag}")
public class ObjectConsumerWithReplyUser implements RocketMQReplyListener<User, User> {

    @Override
    public User onMessage(User user) {
        System.out.printf("------- ObjectConsumerWithReplyUser received: %s \n", user);
        User replyUser = new User();
        replyUser.setUserAge((byte) 10);
        replyUser.setUserName("replyUserName");
        return replyUser;
    }
}
