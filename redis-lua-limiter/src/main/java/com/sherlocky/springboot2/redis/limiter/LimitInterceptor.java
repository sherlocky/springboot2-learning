package com.sherlocky.springboot2.redis.limiter;

import com.google.common.collect.ImmutableList;
import com.sherlocky.common.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 限流切面实现
 */
@Aspect
@Configuration
public class LimitInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LimitInterceptor.class);
    private static final String UNKNOWN = "unknown";
    private final RedisTemplate<String, Serializable> limitRedisTemplate;
    private final RedisScript<Number> redisRequestRateLimiterScript;

    @Autowired
    public LimitInterceptor(RedisTemplate<String, Serializable> limitRedisTemplate, RedisScript redisRequestRateLimiterScript) {
        this.limitRedisTemplate = limitRedisTemplate;
        this.redisRequestRateLimiterScript = redisRequestRateLimiterScript;
    }

    /**
     * @param pjp
     * @description 切面
     */
    @Around("execution(public * *(..)) && @annotation(com.sherlocky.springboot2.redis.limiter.Limiter)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limiter limitAnnotation = method.getAnnotation(Limiter.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        String key;
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();

        /**
         * 根据限流类型获取不同的key ,如果不传我们会以方法名作为key
         */
        switch (limitType) {
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }

        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix(), key));
        Number count = limitRedisTemplate.execute(redisRequestRateLimiterScript, keys, limitCount, limitPeriod);
        logger.info("Access try count is {} for name = {} and key = {}", count, name, key);
        if (count != null && count.intValue() <= limitCount) {
            return pjp.proceed();
        } else {
            throw new LimitException("You have been dragged into the blacklist");
        }
    }

    /**
     * @description 获取id地址
     */
    public String getIpAddress() {
        return HttpUtils.getClientIp(
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest());
    }
}