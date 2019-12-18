package com.sherlocky.springboot2.integration.lock;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

/**
 * 测试 Spring integration redis 的一些简单用法
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2IntegrationLockTest {
    @Autowired
    RedisLockRegistry redisLockRegistry;

    /**
     * 【强制】在使用阻塞等待获取锁的方式中，必须在 try 代码块之外，并且在加锁方法与 try 代
     * 码块之间没有任何可能抛出异常的方法调用，避免加锁成功后，在 finally 中无法解锁。
     *
     * 说明一：如果在 lock 方法与 try 代码块之间的方法调用抛出异常，那么无法解锁，造成其它线程无法成功
     * 获取锁。
     * 说明二：如果 lock 方法在 try 代码块之内，可能由于其它方法抛出异常，导致在 finally 代码块中，
     * unlock 对未加锁的对象解锁，它会调用 AQS 的 tryRelease 方法（取决于具体实现类），抛出
     * IllegalMonitorStateException 异常。
     * 说明三：在 Lock 对象的 lock 方法实现中可能抛出 unchecked 异常，产生的后果与说明二相同。
     *
     * 【强制】在使用尝试机制来获取锁的方式中，进入业务代码块之前，必须先判断当前线程是否持有锁。锁的释放规则与锁的阻塞等待方式相同。
     * 说明：Lock 对象的 unlock 方法在执行时，它会调用 AQS 的 tryRelease 方法（取决于具体实现类），如果
     * 当前线程不持有锁，则抛出 IllegalMonitorStateException 异常。
     *
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        Lock lock = redisLockRegistry.obtain(RedisLockConfiguration.LOCK_KEY);
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
