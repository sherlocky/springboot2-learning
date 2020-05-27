package com.sherlocky.springboot2.redis.limiter;

import com.sherlocky.common.exception.BaseException;

/**
 * 定义一个限流专用的异常
 * @author: zhangcx
 * @date: 2020/5/27 14:58
 * @since:
 */
public class LimitException extends BaseException {
    public LimitException() {
        super();
    }

    public LimitException(String message) {
        super(message);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
