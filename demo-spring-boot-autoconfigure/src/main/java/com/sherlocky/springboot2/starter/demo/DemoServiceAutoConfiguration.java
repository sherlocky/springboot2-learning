package com.sherlocky.springboot2.starter.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DemoService 自动配置类
 * @author: zhangcx
 * @date: 2018/12/8 22:13
 */
@Configuration
@EnableConfigurationProperties(DemoServiceProperties.class)
// 判断DemoService这个类在不在类路径中
@ConditionalOnClass(DemoService.class)
// 当设置 demo=enabled 的情况下，如果没有设置则默认为 true，即符合条件
@ConditionalOnProperty(prefix = "demo", value = "enabled", matchIfMissing = true)
public class DemoServiceAutoConfiguration {

    @Autowired
    private DemoServiceProperties properties;

    @Bean
    @ConditionalOnMissingBean//(DemoService.class) 当容器中没有这个Bean时，才自动配置这个Bean
    public DemoService demoService() {
        DemoService demoService = new DemoService();
        demoService.setName(properties.getName());
        return demoService;
    }
}
