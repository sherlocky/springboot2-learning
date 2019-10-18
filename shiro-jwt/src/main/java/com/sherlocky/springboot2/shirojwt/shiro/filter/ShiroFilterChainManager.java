package com.sherlocky.springboot2.shirojwt.shiro.filter;


import com.sherlocky.springboot2.shirojwt.service.AccountService;
import com.sherlocky.springboot2.shirojwt.shiro.config.RestPathMatchingFilterChainResolver;
import com.sherlocky.springboot2.shirojwt.shiro.constant.ShiroConstants;
import com.sherlocky.springboot2.shirojwt.shiro.provider.ShiroFilterRulesProvider;
import com.sherlocky.springboot2.shirojwt.shiro.rule.RolePermRule;
import com.sherlocky.springboot2.shirojwt.support.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter 链管理器
 */
@Component
@Slf4j
public class ShiroFilterChainManager {
    @Autowired
    private ShiroFilterRulesProvider shiroFilterRulesProvider;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AccountService accountService;

    /**
     * 初始化获取过滤器链
     *
     * @return java.util.Map<java.lang.String, javax.servlet.Filter>
     */
    public Map<String, Filter> initFiltersMap() {
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("auth", new PasswordAccessControlFilter(redisTemplate));
        filters.put("jwt", new JwtPathMatchingFilter(redisTemplate, accountService));
        return filters;
    }

    /**
     * 初始化获取过滤器链规则
     *
     * @return
     */
    public Map<String, String> initFilterChainDefinitionMap() {
        Map<String, String> filterChain = new LinkedHashMap<>();
        /** TODO 应该设置到 配置文件中 */
        // -------------anon 默认过滤器忽略的URL
        List<String> defalutAnon = Arrays.asList("/static/**", "/css/**", "/js/**", "/image/**");
        defalutAnon.forEach(ignored -> filterChain.put(ignored, ShiroConstants.ANON));
        // -------------auth 默认需要认证过滤器的URL 走auth--PasswordFilter
        List<String> defalutAuth = Arrays.asList("/account/**");
        defalutAuth.forEach(auth -> filterChain.put(auth, ShiroConstants.AUTH));
        // -------------dynamic 动态URL
        if (shiroFilterRulesProvider == null) {
            return filterChain;
        }
        List<RolePermRule> rolePermRules = this.shiroFilterRulesProvider.loadRolePermRules();
        if (CollectionUtils.isEmpty(rolePermRules)) {
            return filterChain;
        }
        rolePermRules.forEach(rule -> {
            String chain = rule.toFilterChain();
            if (null != chain) {
                filterChain.putIfAbsent(rule.getUrl(), chain);
            }
        });
        return filterChain;
    }

    /**
     * TODO 不明所以？
     * 动态重新加载过滤链规则
     */
    public void reloadFilterChain() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringContextHolder.getBean(ShiroFilterFactoryBean.class);
        AbstractShiroFilter abstractShiroFilter = null;
        try {
            abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            RestPathMatchingFilterChainResolver filterChainResolver = (RestPathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
            DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
            filterChainManager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(this.initFilterChainDefinitionMap());
            shiroFilterFactoryBean.getFilterChainDefinitionMap().forEach((k, v) -> filterChainManager.createChain(k, v));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
