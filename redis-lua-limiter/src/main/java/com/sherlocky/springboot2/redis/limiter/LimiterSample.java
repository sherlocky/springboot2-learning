package com.sherlocky.springboot2.redis.limiter;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试调用方法限流
 */
@Component
public class LimiterSample {
    private static final AtomicInteger ATOMIC_INTEGER_1 = new AtomicInteger();
    private static final AtomicInteger ATOMIC_INTEGER_2 = new AtomicInteger();
    private static final AtomicInteger ATOMIC_INTEGER_3 = new AtomicInteger();

    @Limiter(name = "defaultLimit", prefix = "limiter:test:", key = "default", period = 10, count = 3)
    public int defaultLimitTest() {
        return ATOMIC_INTEGER_1.incrementAndGet();
    }

    @Limiter(name = "customerLimit", prefix = "limiter:test:", key = "customer", period = 10, count = 3, limitType = LimitType.CUSTOMER)
    public int customerLimitTest() {
        return ATOMIC_INTEGER_2.incrementAndGet();
    }

    @Limiter(name = "ipLimit", prefix = "limiter:test:", key = "ip", period = 10, count = 3, limitType = LimitType.IP)
    public int ipLimitTest() {
        return ATOMIC_INTEGER_3.incrementAndGet();
    }
}