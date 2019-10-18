package com.sherlocky.springboot2.shirojwt.shiro.config;

import com.sherlocky.springboot2.shirojwt.shiro.filter.ShiroFilterChainManager;
import com.sherlocky.springboot2.shirojwt.shiro.realm.RealmsManager;
import com.sherlocky.springboot2.shirojwt.shiro.realm.UserModularRealmAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shiro 配置
 * @author Sherlock
 */
@Configuration
@Slf4j
public class ShiroConfiguration {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroFilterChainManager filterChainManager) {
        RestShiroFilterFactoryBean shiroFilterFactoryBean = new RestShiroFilterFactoryBean();
        // 使用自定义的 RestShiroFilterFactoryBean，继承了 ShiroFilterFactoryBean
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 设置过滤器链
        shiroFilterFactoryBean.setFilters(filterChainManager.initFiltersMap());
        // 过滤器链规则
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainManager.initFilterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(RealmsManager realmsManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 使用自定义的 多模块Realm认证器
        securityManager.setAuthenticator(new UserModularRealmAuthenticator());
        securityManager.setRealms(realmsManager.initListRealms());

        // 无状态subjectFactory设置，禁用 session
        DefaultSessionStorageEvaluator evaluator = (DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager.getSubjectDAO()).getSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(Boolean.FALSE);
        // 使用自定义无状态 WebSubjectFactory，禁用 session
        StatelessWebSubjectFactory subjectFactory = new StatelessWebSubjectFactory();
        securityManager.setSubjectFactory(subjectFactory);

        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }
}