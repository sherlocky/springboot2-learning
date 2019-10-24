package com.sherlocky.springboot2.shiro.config;

import com.sherlocky.springboot2.shiro.realm.UserRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
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
 *
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
        /** 顺序判断 */
        // anon 任何人都能访问
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/user/login", "anon");
        chainDefinition.addPathDefinition("/", "anon");
        chainDefinition.addPathDefinition("/css/**", "anon");
        chainDefinition.addPathDefinition("/js/**", "anon");
        chainDefinition.addPathDefinition("/img/**", "anon");
        chainDefinition.addPathDefinition("/favicon.ico", "anon");
        // 过滤链定义，从上向下顺序执行，一般将/**放在最为下边。这是一个坑呢，一不小心代码就不好使了！
        // authc 认证成功后才能访问
        chainDefinition.addPathDefinition("/**", "authc");
        // 登出过滤器，其中的具体的退出代码Shiro已经替我们实现了
        chainDefinition.addPathDefinition("/logout", "logout");
        return chainDefinition;
    }

    // 内存缓存管理
    @Bean
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 加盐的密码加密验证 匹配器
     * 与 {@link com.sherlocky.springboot2.shiro.pojo.UserPO#passwordEncrypt(String, String)} 相对应
     * <p>由于我们的密码校验交给Shiro的{@link org.apache.shiro.authc.SimpleAuthenticationInfo} 进行处理了</p>
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

    /**
     * 开启shiro aop注解支持。
     * 使用代理方式，所以需要开启代码支持;
     * <p>开启后可以在 controller 类的方法上使用
     * {@link org.apache.shiro.authz.annotation.RequiresPermissions RequiresPermissions},
     * {@link org.apache.shiro.authz.annotation.RequiresRoles RequiresRoles} 注解</p>
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 如果使用 thymeleaf shiro 标签，需要配置Shiro方言支持
     * import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
     */
    /**
     @Bean public ShiroDialect shiroDialect() {
     return new ShiroDialect();
     }
     */

    /**
     * 已在 application-shiro.properties 中配置了 loginUrl, successUrl, unauthorizedUrl
     @Bean public ShiroFilterFactoryBean shirFilter() {
     ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
     shiroFilterFactoryBean.setSecurityManager(securityManager());
     // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
     shiroFilterFactoryBean.setLoginUrl("/login");
     // 登录成功后要跳转的链接
     shiroFilterFactoryBean.setSuccessUrl("/index");
     //未授权界面;
     shiroFilterFactoryBean.setUnauthorizedUrl("/403");
     // shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
     return shiroFilterFactoryBean;
     }
     */
}
