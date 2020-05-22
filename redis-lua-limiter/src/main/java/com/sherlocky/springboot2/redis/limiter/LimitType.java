package com.sherlocky.springboot2.redis.limiter;

/**
 * 限流类型枚举类
 */
public enum LimitType {
    /**
     * 自定义key
     */
    CUSTOMER,

    /**
     * 请求者IP
     */
    IP;
}