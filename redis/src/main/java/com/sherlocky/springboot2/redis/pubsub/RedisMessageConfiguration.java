package com.sherlocky.springboot2.redis.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 装配 RedisMessage 监听容器相关的 Beans （也可直接定义在SpringBoot的 Application 启动类中）
 * <p>
 *  <li>@Configuration： 可理解为xml里面的<beans>标签</li>
 *  <li>@Bean： 可理解为xml里面的<bean>标签</li>
 * </p>
 * @author: zhangcx
 * @date: 2019/1/16 16:26
 */
@Configuration
public class RedisMessageConfiguration {
    @Autowired
    private RedisTemplate redisTemplate = null;
    // Redis 连接工厂
    @Autowired
    private RedisConnectionFactory connectionFactory = null;
    // Redis 消息监昕器
    @Autowired
    private MessageListener redisMsgListener = null;
    // 任务池
    private ThreadPoolTaskScheduler taskScheduler = null ;

    /**
     * 创建任务池，运行线程等待处理redis的消息
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler initTaskScheduler() {
        if (taskScheduler != null) {
            return taskScheduler;
        }
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return taskScheduler;
    }

    /**
     * 定义 Redis 的监听容器
     * @return 监听容器
     */
    @Bean
    public RedisMessageListenerContainer initRedisListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // redis 连接工厂
        container.setConnectionFactory(connectionFactory);
        // 设置运行任务池
        container.setTaskExecutor(taskScheduler);
        // 定义监听主题，名称为 topic1
        Topic topic = new ChannelTopic("test:springboot2:topic");
        // 使用监听器监听主题消息
        container.addMessageListener(redisMsgListener, topic);
        return container;
    }
}
