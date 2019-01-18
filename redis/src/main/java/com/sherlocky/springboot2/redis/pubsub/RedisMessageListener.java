package com.sherlocky.springboot2.redis.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Redis 消息监听器
 * @author: zhangcx
 * @date: 2019/1/16 15:49
 */
@Component
public class RedisMessageListener implements MessageListener {

    /**
     * 得到消息后处理
     * @param message redis 发来的消息
     * @param pattern 主题名称
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 消息体
        String body = new String(message.getBody());
        // 主题(Channel)名称
        String channel = new String(pattern);

        System.out.println("接收到了消息->");
        System.out.println("Channel：" + channel);
        System.out.println("消息体：" + body);
    }
}
