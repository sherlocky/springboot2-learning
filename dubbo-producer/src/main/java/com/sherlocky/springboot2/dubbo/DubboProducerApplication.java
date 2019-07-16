package com.sherlocky.springboot2.dubbo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: zhangcx
 * @date: 2019/7/15 14:58
 */
@EnableDubbo(scanBasePackages  = "com.sherlocky.springboot2.dubbo.service")
@SpringBootApplication
public class DubboProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProducerApplication.class, args);
    }
}
