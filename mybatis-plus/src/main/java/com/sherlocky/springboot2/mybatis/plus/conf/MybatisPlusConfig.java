package com.sherlocky.springboot2.mybatis.plus.conf;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: zhangcx
 * @date: 2019/6/12 15:47
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.sherlocky.springboot2.mybatis.plus.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
