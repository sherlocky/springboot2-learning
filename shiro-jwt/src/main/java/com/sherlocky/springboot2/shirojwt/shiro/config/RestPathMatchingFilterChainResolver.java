package com.sherlocky.springboot2.shirojwt.shiro.config;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Iterator;

/**
 * REST 风格支持的路径匹配过滤器链 处理
 * <p>继承了 PathMatchingFilterChainResolver </p>
 *
 * <p>自定义了REST 风格的 Shiro过滤器链的规则，url = url + "==" + httpMethod</p>
 *
 * <p>
 *   采用RBAC(基于角色的权限访问控制)授权模型，即用户--角色--资源，
 *   用户不直接和权限打交道，角色拥有资源，用户拥有这个角色就有权使用角色所拥有的资源。
 *   所有这里没有权限一说，签发jwt里面也就只有用户所拥有的角色而没有权限。
 *   可参考：https://segmentfault.com/a/1190000014368885
 * </p>
 */
@Slf4j
@NoArgsConstructor
public class RestPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
    private static final int NUM_2 = 2;
    private static final String DEFAULT_PATH_SEPARATOR = "/";

    public RestPathMatchingFilterChainResolver(FilterConfig filterConfig) {
        super(filterConfig);
    }

    /**
     * TODO 重写filterChain匹配
     *
     * @param request
     * @param response
     * @param originalChain
     * @return javax.servlet.FilterChain
     */
    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = this.getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        } else {
            String requestURI = this.getPathWithinApplication(request);
            if (requestURI != null && requestURI.endsWith(DEFAULT_PATH_SEPARATOR)) {
                requestURI = requestURI.substring(0, requestURI.length() - 1);
            }
            Iterator var6 = filterChainManager.getChainNames().iterator();

            String pathPattern;
            boolean flag = true;
            String[] strings = null;
            do {
                if (!var6.hasNext()) {
                    return null;
                }

                pathPattern = (String) var6.next();

                strings = pathPattern.split("==");
                if (strings.length == NUM_2) {
                    // 分割出url+httpMethod,判断httpMethod和request请求的method是否一致,不一致直接false
                    if (WebUtils.toHttp(request).getMethod().toUpperCase().equals(strings[1].toUpperCase())) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                } else {
                    flag = false;
                }
                pathPattern = strings[0];
                if (pathPattern != null && pathPattern.endsWith(DEFAULT_PATH_SEPARATOR)) {
                    pathPattern = pathPattern.substring(0, pathPattern.length() - 1);
                }
            } while (!this.pathMatches(pathPattern, requestURI) || flag);

            if (log.isTraceEnabled()) {
                log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  Utilizing corresponding filter chain...");
            }
            if (strings.length == NUM_2) {
                pathPattern = pathPattern.concat("==").concat(WebUtils.toHttp(request).getMethod().toUpperCase());
            }

            return filterChainManager.proxy(originalChain, pathPattern);
        }
    }

}
