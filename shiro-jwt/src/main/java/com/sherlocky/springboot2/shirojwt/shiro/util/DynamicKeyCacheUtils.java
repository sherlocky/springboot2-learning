package com.sherlocky.springboot2.shirojwt.shiro.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author: zhangcx
 * @date: 2019/10/18 18:58
 */
public class DynamicKeyCacheUtils {
    /**
     * 过期时间 5秒（单位：秒）
     */
    private static final long EXPIRED_SECONDS = 5L;

    private DynamicKeyCacheUtils() {
    }

    /**
     * 从redis 缓存中获取账户jwt
     *
     * @param redisTemplate
     * @param host
     * @param userKey
     * @return jwt
     */
    public static String get(StringRedisTemplate redisTemplate, String host, String userKey) {
        return redisTemplate.opsForValue().get(dynamicKeyCacheKey(host, userKey));
    }

    /**
     * 将账户的 jwt 缓存到 redis, {JWT-SESSION-{account} , jwt}
     *
     * @param redisTemplate
     * @param host
     * @param userKey
     * @param dynamicKey
     */
    public static void set(StringRedisTemplate redisTemplate, String host, String userKey, String dynamicKey) {
        redisTemplate.opsForValue().set(dynamicKeyCacheKey(host, userKey), dynamicKey, EXPIRED_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 获取动态密钥 在redis 缓存中的 Key 值
     */
    private static String dynamicKeyCacheKey(String host, String userKey) {
        return String.format("DYNAMIC_KEY:%s:%s", StringUtils.lowerCase(host), userKey);
    }
}
