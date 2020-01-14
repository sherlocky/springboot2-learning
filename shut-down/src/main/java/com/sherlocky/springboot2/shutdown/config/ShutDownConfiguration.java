package com.sherlocky.springboot2.zookeeper.config;

import com.sherlocky.springboot2.shutdown.bean.ShutDownBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Zookeeper 客户端配置
 *
 * @author Administrator
 */
@Configuration
@Slf4j
public class ShutDownConfiguration {

    @Bean
    public ShutDownBean getShutDownBean() {
        return new ShutDownBean();
    }
}