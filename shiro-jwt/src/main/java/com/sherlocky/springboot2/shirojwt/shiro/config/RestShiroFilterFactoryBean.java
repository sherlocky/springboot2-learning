package com.sherlocky.springboot2.shirojwt.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * rest 风格支持的shiroFilterFactoryBean
 */
@Slf4j
public class RestShiroFilterFactoryBean extends ShiroFilterFactoryBean {
    /**
     * 创建自定义 Shiro 过滤器对象
     *
     * @return
     * @throws Exception
     */
    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("创建自定义 Shiro 过滤器对象。");
        }
        SecurityManager securityManager = this.getSecurityManager();
        if (securityManager == null) {
            throw new BeanInitializationException("SecurityManager 未配置！");
        }
        if (!(securityManager instanceof WebSecurityManager)) {
            throw new BeanInitializationException(String.format("{%s} 未实现 WebSecurityManager 接口", securityManager.getClass()));
        }
        FilterChainManager manager = this.createFilterChainManager();
        RestPathMatchingFilterChainResolver chainResolver = new RestPathMatchingFilterChainResolver();
        // 重写ShiroFilterFactoryBean，使其使用改造后的chainResolver
        chainResolver.setFilterChainManager(manager);
        return new SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }

    /**
     * copy 自 {@link ShiroFilterFactoryBean} 的 SpringShiroFilter 静态内部类实现
     */
    private static final class SpringShiroFilter extends AbstractShiroFilter {
        protected SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            super();
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("WebSecurityManager 未配置！");
            }
            this.setSecurityManager(webSecurityManager);
            if (resolver != null) {
                this.setFilterChainResolver(resolver);
            }
        }
    }
}
