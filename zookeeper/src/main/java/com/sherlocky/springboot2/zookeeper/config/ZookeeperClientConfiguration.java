package com.sherlocky.springboot2.zookeeper.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Zookeeper 客户端配置
 *
 * @author Administrator
 */
@Configuration
@Slf4j
public class ZookeeperClientConfiguration {
    @Value("${zookeeper.url}")
    private String zookeeperUrl;

    @Bean
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);

        //创建zookeeper客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperUrl)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        // CuratorFrameworkFactory.newClient(zookeeperUrl, retryPolicy);
        client.start();
        return client;
    }
}