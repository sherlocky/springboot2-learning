package com.sherlocky.springboot2.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 简单redis使用示例
 */
@SpringBootApplication
//@EnableCaching
public class Springboot2RedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot2RedisApplication.class, args);
    }
    //＠PostConstruct
}
