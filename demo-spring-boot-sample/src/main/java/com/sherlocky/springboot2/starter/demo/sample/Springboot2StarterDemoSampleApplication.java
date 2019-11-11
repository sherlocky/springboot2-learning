package com.sherlocky.springboot2.starter.demo.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * @author: zhangcx
 * @date: 2018/12/8 22:36
 */
@SpringBootApplication
public class Springboot2StarterDemoSampleApplication implements CommandLineRunner {
    @Autowired
    private com.sherlocky.springboot2.starter.demo.DemoService demoService;

    public static void main(String[] args) {
        SpringApplication.run(Springboot2StarterDemoSampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        demoService.sayHello();
    }
}
