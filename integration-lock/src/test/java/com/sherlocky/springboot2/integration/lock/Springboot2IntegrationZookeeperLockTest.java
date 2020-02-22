package com.sherlocky.springboot2.integration.lock;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 测试 Spring integration zookeeper lock 的一些简单用法
 * <p>
 * zookeeper 实现获取到的锁为{@link org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry.ZkLock }
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("zookeeper")
public class Springboot2IntegrationZookeeperLockTest {
    @Autowired
    ZookeeperLockRegistry zookeeperLockRegistry;

    /**
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        Lock lock = zookeeperLockRegistry.obtain(LockConstants.LOCK_KEY);

        // 阻塞获取锁
        info("###" + System.currentTimeMillis() + "###阻塞获取锁~");
        lock.lock();
        info("###" + System.currentTimeMillis() + "###成功拿到锁~");
        try {
            //do somethings
            info("do somethings~");
        } finally {
            lock.unlock();
            info("###" + System.currentTimeMillis() + "###释放锁~");
        }

        boolean b1 = false;
        try {
            info("###" + System.currentTimeMillis() + "###b1开始争抢锁~");
            b1 = lock.tryLock(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("###" + System.currentTimeMillis() + "###b1获取锁失败 ： ");
        }
        // 只有获取到锁了，才能进入业务代码块
        if (b1) {
            try {
                // do somethings
                info("###" + System.currentTimeMillis() + "###b1 is : %s", b1);
            } finally {
                // finally 中一定要释放锁
                lock.unlock();
                info("###" + System.currentTimeMillis() + "###b1解锁~");
            }
        } else {
            info("###" + System.currentTimeMillis() + "###b1未获得锁~");
        }

        TimeUnit.SECONDS.sleep(1);

        boolean b2 = false;
        try {
            info("###" + System.currentTimeMillis() + "###b2开始争抢锁~");
            b2 = lock.tryLock(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("###" + System.currentTimeMillis() + "###b2获取锁失败 ： ");
        }
        // 只有获取到锁了，才能进入业务代码块
        if (b2) {
            try {
                // do somethings
                info("###" + System.currentTimeMillis() + "###b2 is : %s", b2);
            } finally {
                // finally 中一定要释放锁
                lock.unlock();
                info("###" + System.currentTimeMillis() + "###b2解锁~");
            }
        } else {
            info("###" + System.currentTimeMillis() + "###b2未获得锁~");
        }
    }

    private void info(String msg) {
        System.out.println(msg);
    }

    private void info(String msg, String str) {
        System.out.println(String.format(msg, str));
    }

    private void info(String msg, Object obj) {
        System.out.println(String.format(msg, JSON.toJSONString(obj)));
    }
}
