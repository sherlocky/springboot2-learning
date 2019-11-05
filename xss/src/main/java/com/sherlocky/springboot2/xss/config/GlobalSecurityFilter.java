package com.sherlocky.springboot2.xss.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class GlobalSecurityFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * 不处理 contentType 为 multipart/form-data 的请求。
         * 涉及到文件上传的业务，可以通过其他方式做文件魔术数字校验，文件后缀校验，文件大小校验等方式，
         * 没必要在这个地方校验 XSS 内容。
         */
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType) && contentType.contains("multipart/form-data")) {
            filterChain.doFilter(request, response);
            return;
        }
        GlobalSecurityRequestWrapper xssHttpServletRequestWrapper = new GlobalSecurityRequestWrapper(request);
        filterChain.doFilter(xssHttpServletRequestWrapper, response);
        //TODO
        /*String requestBody = IOUtils.toString(request.getInputStream(), "UTF-8");
        if (requestBody != null && !requestBody.equalsIgnoreCase(HtmlUtils.htmlEscape(requestBody))) {
            throw new RuntimeException();
        }
        filterChain.doFilter(request, response);*/
    }
}