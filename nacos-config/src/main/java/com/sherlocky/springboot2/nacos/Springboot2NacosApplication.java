package com.sherlocky.springboot2.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * 简单 Nacos config 使用示例
 * @author: zhangcx
 * @date: 2020/4/13 18:46
 */
@SpringBootApplication
//@EnableNacosConfig
public class Springboot2NacosApplication {
    @Value("${connectTimeoutInMills:5000}")
    private int connectTimeoutInMills;

    public static void main(String[] args) {
        SpringApplication.run(Springboot2NacosApplication.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("###################################");
        System.out.println(connectTimeoutInMills);
        System.out.println("###################################");
    }
}
