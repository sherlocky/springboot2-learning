package com.sherlocky.springboot2.shirojwt.controller;

import com.sherlocky.springboot2.shirojwt.util.RequestResponseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * controller 基础抽象类
 */
public abstract class BaseController {

    /**
     * 获得来的request中的 key value数据封装到map返回
     *
     * @param request
     * @return java.util.Map<java.lang.String, java.lang.String>
     */
    @SuppressWarnings("rawtypes")
    protected Map<String, String> getRequestParameter(HttpServletRequest request) {
        return RequestResponseUtil.getRequestParameters(request);
    }

    protected Map<String, String> getRequestBody(HttpServletRequest request) {
        return RequestResponseUtil.getRequestBodyMap(request);
    }

    protected Map<String, String> getRequestHeader(HttpServletRequest request) {
        return RequestResponseUtil.getRequestHeaders(request);
    }
}
