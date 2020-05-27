package com.sherlocky.springboot2.redis.limiter;

import java.lang.annotation.*;

/**
 * 自定义一个 @Limiter 注解，注解类型为 ElementType.METHOD 即作用于方法上
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limiter {
    /**
     * 名字
     */
    String name() default "";

    /**
     * key
     */
    String key() default "";

    /**
     * Key的前缀
     */
    String prefix() default "";

    /**
     * 请求限制时间段 - 给定的时间范围 单位(秒)
     */
    int period();

    /**
     * 一定时间内最多访问次数（在period这个时间段内允许放行请求的次数）
     */
    int count();

    /**
     * 限流的类型(用户自定义key 或者 请求ip)
     */
    LimitType limitType() default LimitType.CUSTOMER;
}