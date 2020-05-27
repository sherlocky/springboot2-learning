package com.sherlocky.springboot2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Java内部也可以通过原子类计数器AtomicInteger、Semaphore信号量来做简单的限流
 * @author: zhangcx
 * @date: 2020/5/22 13:39
 * @since:
 */
public class CounterLimiter {
    // 限流的个数
    private int maxCount = 10;
    // 指定的时间内
    private long interval = 60;
    // 原子类计数器
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    // 起始时间
    private long startTime = System.currentTimeMillis();

    public boolean limit(int maxCount, int interval) {
        atomicInteger.addAndGet(1);
        if (atomicInteger.get() == 1) {
            startTime = System.currentTimeMillis();
            atomicInteger.addAndGet(1);
            return true;
        }
        // 超过了间隔时间，直接重新开始计数
        if (System.currentTimeMillis() - startTime > interval * 1000) {
            startTime = System.currentTimeMillis();
            atomicInteger.set(1);
            return true;
        }
        // 还在间隔时间内,check有没有超过限流的个数
        if (atomicInteger.get() > maxCount) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        CounterLimiter limiter = new CounterLimiter();
        IntStream.range(0, 13).forEach(i -> {
            System.out.println(limiter.limit(10, 1));
        });
    }
}
