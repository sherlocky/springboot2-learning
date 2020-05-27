package com.sherlocky.springboot2.redis.limiter;

import com.sherlocky.common.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

/**
 * @author: zhangcx
 * @date: 2020/5/27 15:10
 * @since:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2RedisLimiterTest extends BaseTest {
    @Autowired
    private LimiterSample limiterSample;

    @Test(expected = LimitException.class)
    public void test1() {
        IntStream.range(0, 4).forEach(i -> {
            println(limiterSample.defaultLimitTest());
        });
    }

    @Test(expected = LimitException.class)
    public void test2() {
        IntStream.range(0, 4).forEach(i -> {
            println(limiterSample.customerLimitTest());
        });

    }

    @Test(expected = LimitException.class)
    public void test3() {
        IntStream.range(0, 4).forEach(i -> {
            println(limiterSample.ipLimitTest());
        });
    }
}
