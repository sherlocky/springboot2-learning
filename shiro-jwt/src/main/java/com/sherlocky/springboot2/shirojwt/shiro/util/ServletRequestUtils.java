package com.sherlocky.springboot2.shirojwt.shiro.util;

import com.sherlocky.springboot2.shirojwt.util.IpUtil;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;

/**
 * Shiro ServletRequest 工具类
 * @author: zhangcx
 * @date: 2019/10/18 18:33
 */
public class ServletRequestUtils {
    private ServletRequestUtils() {
    }

    /**
     * 从request中获取用户host
     * @param request
     * @return
     */
    public static String host(ServletRequest request) {
        return IpUtil.getIpFromRequest(WebUtils.toHttp(request));
    }
}
