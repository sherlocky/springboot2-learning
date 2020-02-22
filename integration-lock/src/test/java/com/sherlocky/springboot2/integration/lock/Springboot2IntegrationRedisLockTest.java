package com.sherlocky.springboot2.integration.lock;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

/**
 * 测试 Spring integration redis lock 的一些简单用法
 * <p>
 * Redis实现获取到的锁为{@link org.springframework.integration.redis.util.RedisLockRegistry.RedisLock}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("redis")
public class Springboot2IntegrationRedisLockTest {
    @Autowired
    RedisLockRegistry redisLockRegistry;

    /**
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        Lock lock = redisLockRegistry.obtain(LockConstants.LOCK_KEY);
        boolean b1 = false;
        try {
            System.out.println("###" + System.currentTimeMillis() + "###b1开始争抢锁~");
            b1 = lock.tryLock(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("###" + System.currentTimeMillis() + "###b1获取锁失败 ： ");
        }
        // 只有获取到锁了，才能进入业务代码块
        if (b1) {
            try {
                // do somethings
                info("###" + System.currentTimeMillis() + "###b1 is : {}", b1);
            } finally {
                // finally 中一定要释放锁
                lock.unlock();
                System.out.println("###" + System.currentTimeMillis() + "###b1解锁~");
            }
        }

        TimeUnit.SECONDS.sleep(1);

        boolean b2 = false;
        try {
            System.out.println("###" + System.currentTimeMillis() + "###b2开始争抢锁~");
            b2 = lock.tryLock(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("###" + System.currentTimeMillis() + "###b2获取锁失败 ： ");
        }
        // 只有获取到锁了，才能进入业务代码块
        if (b2) {
            try {
                // do somethings
                info("###" + System.currentTimeMillis() + "###b2 is : {}", b2);
            } finally {
                // finally 中一定要释放锁
                lock.unlock();
                System.out.println("###" + System.currentTimeMillis() + "###b2解锁~");
            }
        }
    }

    @Test
    public void testMultiThread() throws InterruptedException, TimeoutException {
        TestLockThread tl1 = new TestLockThread(redisLockRegistry);
        TestLockThread tl2 = new TestLockThread(redisLockRegistry);
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.execute(tl1);
        service.execute(tl2);
        /**
         * shutdown方法：平滑的关闭 ExecutorService。
         * 当此方法被调用时，ExecutorService 停止接收新的任务并且等待已经提交的任务（包含提交正在执行和提交未执行的）执行完成。
         * 当所有提交任务执行完毕，线程池即被关闭。
         */
        service.shutdown();
        /**
         * awaitTermination方法：
         * 接收timeout和TimeUnit两个参数，用于设定超时时间及单位。
         * 当等待超过设定时间时，会监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false。
         * 一般情况下会和shutdown方法组合使用。
         */
        if (!service.awaitTermination(1, TimeUnit.MINUTES)) {
            throw new TimeoutException();
        }
        /** 执行结果如下：
         pool-1-thread-1_1_$$$$$$$$$$$$$$$1553672787958$$$$$$$$$$$$$$$
         pool-1-thread-1_2_###############1553672787958##############
         pool-1-thread-1_3_###############1553672787959##############
         pool-1-thread-2_1_$$$$$$$$$$$$$$$1553672787961$$$$$$$$$$$$$$$             线程2和线程1同时进入run方法
         pool-1-thread-2_2_###############1553672787961##############
         pool-1-thread-2_3_###############1553672787961##############
         pool-1-thread-1_4_###############1553672788672##############              线程1获取锁成功
         pool-1-thread-1 b1 is : {}true
         pool-1-thread-2_4_###############1553672790962##############              线程2获取锁失败，3秒超时(1553672790962-1553672787961)
         pool-1-thread-2 b1 is : {}false
         pool-1-thread-1_5_###############1553672793721##############              线程1获取锁成功，获取后sleep了5秒
         pool-1-thread-2_5_###############1553672795963##############              线程2获取锁失败，也sleep了5秒

         线程2获取锁失败，所以在 unlock 时抛出异常
         Exception in thread "pool-1-thread-2" java.lang.IllegalStateException: You do not own lock at spring-integration-lock:lock
         at org.springframework.integration.redis.util.RedisLockRegistry$RedisLock.unlock(RedisLockRegistry.java:300)
         at com.sherlocky.springboot2.integration.lock.TestLockThread.run(TestLockThread.java:39)
         at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
         at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
         at java.lang.Thread.run(Thread.java:748)

         说明线程2没有拿到锁，证明了分布式锁的存在。
         */
    }

    private void info(String msg, Object obj) {
        System.out.println(msg + JSON.toJSONString(obj));
    }
}
