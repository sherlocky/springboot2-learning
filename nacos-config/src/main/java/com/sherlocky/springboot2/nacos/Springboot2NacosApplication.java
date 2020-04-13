package com.sherlocky.springboot2.nacos;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * 简单 Nacos config 使用示例
 * @author: zhangcx
 * @date: 2020/4/13 18:46
 */
@SpringBootApplication
/** 使用注解加载配置源，并开启自动更新 */
@NacosPropertySource(dataId = "application", groupId = "demo", autoRefreshed = true)
public class Springboot2NacosApplication {
    @NacosValue(value = "${connectTimeoutInMills:5000}", autoRefreshed = true)
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
