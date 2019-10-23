package com.sherlocky.springboot2.shirojwt.shiro.constant;

/**
 * Shiro 有关常量
 * @author: zhangcx
 * @date: 2019/10/18 16:50
 */
public class ShiroConstants {
    /** 角色字符串分隔符 */
    public static final String ROLES_SEPARATOR = ",";
    /** 匿名拦截器 -- 不需要登录就能访问 */
    public static final String FILTER_ANON = "anon";
    /** 认证 需要登录认证才能访问*/
    public static final String FILTER_AUTH = "auth";
}
