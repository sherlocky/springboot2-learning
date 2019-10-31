package com.sherlocky.springboot2.session.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

/**
 * 支持spring session记住我
 *
 * @author: zhangcx
 * @date: 2019/10/30 14:35
 * @since:
 */
@Configuration
public class SpringSessionSecurityConfigure extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.user.name}")
    private String userName;
    @Value("${spring.security.user.password}")
    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*匹配所有路径的*/
        http
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                /**
                 * 1.formLogin系统会自动配置/login页面用于登录
                 * 2.假如登录失败会重定向到login/error/
                 */
                .formLogin()
                .defaultSuccessUrl("/", true)
                .and()
                /*开启记住我功能，登录会添加Cookie,点击注销会删除Cookie*/
                .rememberMe()
                .rememberMeServices(rememberMeServices())
                /* 配置Cookie过期时间 */
                .tokenValiditySeconds(Integer.MAX_VALUE)
        ;
    }

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 从内存中读取认证
                .inMemoryAuthentication()
                // Spring Security 5.0开始必须要设置加密方式
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser(userName).password(new BCryptPasswordEncoder().encode(password)).roles("USER")
        ;
    }
}
