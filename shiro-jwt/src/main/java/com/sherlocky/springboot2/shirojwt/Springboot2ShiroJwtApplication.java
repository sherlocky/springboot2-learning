package com.sherlocky.springboot2.shirojwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Shiro 示例
 */
@SpringBootApplication
@EnableCaching
public class Springboot2ShiroJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot2ShiroJwtApplication.class, args);
    }
}
