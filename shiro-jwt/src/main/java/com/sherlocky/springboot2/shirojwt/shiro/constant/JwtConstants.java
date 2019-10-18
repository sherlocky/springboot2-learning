package com.sherlocky.springboot2.shirojwt.shiro.constant;

/**
 * JWT 相关常量
 * @author: zhangcx
 * @date: 2019/10/18 16:50
 */
public class JwtConstants {
    public static final String SECRET_KEY = "%3Dk^dZ4Wf8Toz*xXpWQwgOL";

    /** 令牌 过期 */
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
    /** 令牌 错误 */
    public static final String TOKEN_ERROR = "TOKEN_ERROR";
    /** 令牌格式 错误 */
    public static final String TOKEN_MALFORMED = "TOKEN_MALFORMED";
    /** 刷新周期时间 10 hours（单位：秒） */
    public static final long REFRESH_PERIOD_TIME_SECONDS = 10 * 60 * 60L;

    /** 令牌缓存前缀 */
    public static final String JWT_CACHE_PREFIX = "JWT";
    /** 令牌Session 缓存前缀 */
    public static final String JWT_SESSION_CACHE_PREFIX = "JWT-SESSION";
}
