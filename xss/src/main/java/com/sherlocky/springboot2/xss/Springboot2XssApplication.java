package com.sherlocky.springboot2.xss;

import com.sherlocky.springboot2.xss.config.GlobalSecurityFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

/**
 * @author: zhangcx
 * @date: 2019/11/04 16:13
 */
@SpringBootApplication
public class Springboot2XssApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot2XssApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(globalSecurityFilter());
        /**
         * TODO
         * URL 过滤 pattern 设置
         */
        // registration.addUrlPatterns("/*");
        registration.setOrder(5);
        return registration;
    }

    @Bean(name = "globalSecurityFilter")
    public Filter globalSecurityFilter() {
        return new GlobalSecurityFilter();
    }
}
