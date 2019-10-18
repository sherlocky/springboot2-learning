package com.sherlocky.springboot2.shirojwt.shiro.util;

import com.sherlocky.springboot2.shirojwt.shiro.constant.JwtConstants;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * jwt session 缓存工具类
 * @author: zhangcx
 * @date: 2019/10/18 18:58
 */
public class JwtSessionCacheUtils {
    private JwtSessionCacheUtils() {
    }

    /**
     * 从redis 缓存中获取账户jwt
     * @param redisTemplate
     * @param account
     * @return jwt
     */
    public static String get(StringRedisTemplate redisTemplate, String account) {
        return redisTemplate.opsForValue().get(jwtSessionCacheKey(account));
    }

    /**
     * 将账户的 jwt 缓存到 redis, {JWT-SESSION-{account} , jwt}
     * @param redisTemplate
     * @param account
     * @param newJwt
     */
    public static void set(StringRedisTemplate redisTemplate, String account, String newJwt) {
        redisTemplate.opsForValue().set(jwtSessionCacheKey(account), newJwt, JwtConstants.REFRESH_PERIOD_TIME_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 获取 JWT Session 在redis中的key
     * <p><code>JWT-SESSION:{account}</code></p>
     *
     * @param keyWithoutPrefix
     * @return
     */
    private static String jwtSessionCacheKey(String keyWithoutPrefix) {
        return String.format("%s:%s", JwtConstants.JWT_SESSION_CACHE_PREFIX, keyWithoutPrefix);
    }

}
