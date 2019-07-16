package com.sherlocky.springboot2.dubbo;

import com.sherlocky.springboot2.dubbo.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 需要先启动 DubboProducerApplication
 * @author: zhangcx
 * @date: 2019/7/15 14:58
 */
@Slf4j
// 消费端此处不声明 @EnableDubbo 也可以使用，暂时不解？
@EnableDubbo
@SpringBootApplication
public class DubboConsumerApplication implements CommandLineRunner {
    @Reference(version = "${base.service.version}")
    private BaseService baseService;

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(baseService.sayHello("我是一个消费者~~"));
    }
}
