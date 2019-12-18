package com.sherlocky.springboot2.redis.lock.distributed;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * Redission 封装的 Redlock 实现分布式锁
 * <p>
 * https://github.com/redisson/redisson#quick-start
 * <p>
 * https://redis.io/topics/distlock
 * <p>
 * https://halo.sherlocky.com/archives/redission/
 * <p>
 *     另外 spring integration 也提供了全局锁，可参见：<a href="https://gitee.com/sherlocky/springboot2-learning/tree/master/integration-lock">integration-lock</a>
 * </p>
 * @author: zhangcx
 * @date: 2019/6/19 14:30
 */
public class RedissionRedlockDemo {

    /**
     * redission 封装的 redlock 算法实现的分布式锁用法，非常简单，跟重入锁（ReentrantLock）有点类似：
     */
    public static void redlock() {
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress("127.0.0.1:6369", "127.0.0.1:6379", "127.0.0.1:6389")
                .setMasterName("masterName")
                .setPassword("password").setDatabase(0);

        // Sync and Async API
        RedissonClient redissonClient = Redisson.create(config);

        // 还可以使用 getFairLock(), getReadWriteLock()
        RLock redLock = redissonClient.getLock("REDLOCK_KEY");
        boolean isLock;
        try {
            isLock = redLock.tryLock();
            // 500ms拿不到锁, 就认为获取锁失败。10000ms即10s是锁失效时间。
            isLock = redLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                //TODO if get lock success, do something;
            }
        } catch (Exception e) {

        } finally {
            // 无论如何, 最后都要解锁（防止某些节点获取到锁但是客户端没有得到响应而导致接下来的一段时间不能被重新获取锁）
            redLock.unlock();
        }
    }
}
