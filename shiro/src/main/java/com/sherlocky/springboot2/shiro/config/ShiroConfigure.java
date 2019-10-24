package com.sherlocky.springboot2.shiro.config;

import com.sherlocky.springboot2.shiro.realm.UserRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Shiro 配置
 * @author: zhangcx
 * @date: 2019/10/23 17:32
 */
@Configuration
@ControllerAdvice
@Slf4j
public class ShiroConfigure {
    /**
     * 统一处理Shiro异常:403
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void authorizationExceptionHandler() {
        log.error("$$$ 没有权限访问该资源");
    }

    @Bean
    public Realm realm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    /**
     * 配置url 拦截定义
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        // anon 任何人都能访问
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/user/login", "anon");
        chainDefinition.addPathDefinition("/", "anon");
        chainDefinition.addPathDefinition("/css/**", "anon");
        chainDefinition.addPathDefinition("/js/**", "anon");
        chainDefinition.addPathDefinition("/img/**", "anon");
        chainDefinition.addPathDefinition("/favicon.ico", "anon");
        // authc 认证成功后才能访问
        chainDefinition.addPathDefinition("/**", "authc");

        chainDefinition.addPathDefinition("/logout", "logout");
        return chainDefinition;
    }

    // 内存缓存管理
    @Bean
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 如果使用 thymeleaf shiro 标签，需要配置Shiro方言支持
     * import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
    */

    /**
     * 加盐的密码加密验证
     * 与 {@link com.sherlocky.springboot2.shiro.pojo.UserPO#passwordEncrypt(String, String)} 相对应
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(6);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }
}
