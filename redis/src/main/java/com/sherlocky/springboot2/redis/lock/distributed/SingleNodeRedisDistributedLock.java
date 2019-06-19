package com.sherlocky.springboot2.redis.lock.distributed;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * <p>分布式锁一般有三种实现方式：1. 数据库乐观锁；2. 基于Redis的分布式锁；3. 基于ZooKeeper的分布式锁</p>
 *
 * <p>这里只介绍：Redis 实现分布式锁，且只考虑 Redis 服务端<b>单机部署</b>的场景。</p>
 *
 * <p>使用第三方开源组件 Jedis 实现 Redis 客户端</p>
 *
 * <p>分布式锁的可靠性要同时满足以下四个条件：</p>
 * <ul>
 *     <li>互斥性: 在任意时刻，只有一个客户端能持有锁。</li>
 *     <li>不会发生死锁： 即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能正常加锁。</li>
 *     <li>具有容错性： 只要大部分的 Redis 节点正常运行，客户端就可以加锁和解锁。</li>
 *     <li>解铃还须系铃人： 加锁和解锁必须是同一个客户端，客户端不能解锁其他客户端加的锁。</li>
 * </ul>
 * @author: zhangcx
 * @date: 2019/6/19 11:27
 */
public class SingleNodeRedisDistributedLock {
    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 事实上本类实现琐（正确姿势）最大的缺点就是它加锁时只作用在一个 Redis 节点上，
     * 即使Redis通过 sentinel 保证高可用，如果这个 master 节点由于某些原因发生了主从切换，那么就会出现锁丢失的情况：
     *
     *  在Redis 的 master 节点上拿到了锁;但是这个加锁的 key 还没有同步到 slave 节点;
     *  master 故障，发生故障转移；slave 节点升级为 master 节点；导致锁丢失。
     */

    /**
     * 尝试获取分布式锁 （正确姿势）
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        /**
         * 使用的 set() 共有 5 个参数
         * key： 是唯一的，使用 key 来作为锁
         * value：通过给value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据（第四个条件）。
         * nxxx： 使用的是 NX，意思是 SET IF NOT EXIST，即当key不存在时，进行set操作；若key已经存在，则不做任何操作；
         * expx： 使用的是PX，意思是我们要给这个 key 加一个过期的设置，具体时间由第五个参数决定。
         * time： 与第四个参数相呼应，代表key的过期时间。
         */
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
        /**
         * 执行上面的set()方法就只会导致两种结果：
         * 1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。
         * 2. 已有锁存在，不做任何操作。
         */
    }

    /**
     * 释放分布式锁 （正确姿势）
     *
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        // 执行 eval() 方法执行 lua 脚本可以确保原子性
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        // 首先获取锁对应的 value 值，检查是否与 requestId 相等，如果相等则删除锁（解锁）
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 获取锁 错误姿势 1
     * @param jedis
     * @param lockKey
     * @param requestId
     * @param expireTime
     */
    private static void wrongGetLock1(Jedis jedis, String lockKey, String requestId, int expireTime) {
        Long result = jedis.setnx(lockKey, requestId);
        if (result == 1) {
            // 若在这里程序突然崩溃，则无法设置过期时间，将发生死锁
            jedis.expire(lockKey, expireTime);
        }
        /**
         * 这是两条Redis命令，不具有原子性。
         * 之所以有人这样实现，是因为低版本的jedis并不支持多参数的set()方法。
         */
    }

    /**
     * 释放锁 错误姿势 1
     * @param jedis
     * @param lockKey
     */
    private static void wrongReleaseLock1(Jedis jedis, String lockKey) {
        // 最常见的解锁代码就是直接使用 jedis.del()方法删除锁，
        // 这种不先判断锁的拥有者而直接解锁的方式，会导致任何客户端都可以随时进行解锁，即使这把锁不是它的。
        jedis.del(lockKey);
    }

    /**
     * 获取锁 错误姿势 2
     * @param jedis
     * @param lockKey
     * @param expireTime
     * @return
     */
    private static boolean wrongGetLock2(Jedis jedis, String lockKey, int expireTime) {
        long expires = System.currentTimeMillis() + expireTime;
        String expiresStr = String.valueOf(expires);
        // 如果当前锁不存在，返回加锁成功
        if (jedis.setnx(lockKey, expiresStr) == 1) {
            return true;
        }
        // 如果锁存在，获取锁的过期时间
        String currentValueStr = jedis.get(lockKey);
        if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
            // 锁已过期，获取上一个锁的过期时间，并设置现在锁的过期时间
            String oldValueStr = jedis.getSet(lockKey, expiresStr);
            if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                // 考虑多线程并发的情况，只有一个线程的设置值和当前值相同，它才有权利加锁
                return true;
            }
        }
        // 其他情况，一律返回加锁失败
        return false;
        /**
         * 1. 由于是客户端自己生成过期时间，所以需要强制要求分布式下每个客户端的时间必须同步。
         * 2. 当锁过期的时候，如果多个客户端同时执行 jedis.getSet() 方法，那么虽然最终只有一个客户端可以加锁，但是这个客户端的锁的过期时间可能被其他客户端覆盖。
         * 3. 锁不具备拥有者标识，即任何客户端都可以解锁。
         */
    }

    /**
     * 释放锁 错误姿势 2
     * @param jedis
     * @param lockKey
     * @param requestId
     */
    private static void wrongReleaseLock2(Jedis jedis, String lockKey, String requestId) {
        // 判断加锁与解锁是不是同一个客户端
        if (requestId.equals(jedis.get(lockKey))) {
            // 若在此时，这把锁突然不是这个客户端的，则会误解锁
            jedis.del(lockKey);
        }
        /**
         * 这种解锁代码乍一看也是没问题，与正确姿势差不多，唯一区别的是分成两条命令去执行。
         * 问题在于如果调用 jedis.del() 方法的时候，这把锁已经不属于当前客户端的时候会解除他人加的锁。
         *  比如客户端 A 加锁，一段时间之后客户端A解锁，在执行 jedis.del() 之前，锁突然过期了，
         *  此时客户端 B 尝试加锁成功，然后客户端A再执行 del() 方法，则将客户端 B 的锁给解除了。
         */
    }
}
