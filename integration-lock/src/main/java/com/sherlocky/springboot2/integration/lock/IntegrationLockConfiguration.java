package com.sherlocky.springboot2.integration.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;

/**
 * @author Administrator
 */
@Configuration
public class IntegrationLockConfiguration {
    @Value("${zookeeper.url}")
    private String zookeeperUrl;

    /**
     * 根据 profile 分别注入各自的 全局锁实现
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @Profile("redis")
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, LockConstants.REGISTRY_KEY);
    }

    @Bean
    @Profile("zookeeper")
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperUrl, retryPolicy);
        client.start();
        return client;
    }

    @Bean
    @Profile("zookeeper")
    public ZookeeperLockRegistry zookeeperLockRegistry() {
        // 默认锁的根节点为 /SpringIntegration-LockRegistry
        return new ZookeeperLockRegistry(getCuratorFramework());
    }
}