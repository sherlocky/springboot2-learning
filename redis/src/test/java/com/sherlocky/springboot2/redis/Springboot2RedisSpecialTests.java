package com.sherlocky.springboot2.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 测试 Spring data redis 的一些特殊用法
 * @author: zhangcx
 * @date: 2019/1/16 10:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2RedisSpecialTests {
    // 添加一个统一的key前缀，测试方便
    private final String KEY_PREFIX = "test:springboot2:";

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 测试 redis 事务（和数据库事务有不同）
     */
    @Test
    public void testTransaction() {
        // 先删除
        redisTemplate.delete(key("transaction:key1"));
        redisTemplate.delete(key("transaction:key2"));

        redisTemplate.opsForValue().set(key("transaction:key1"), "val1");

        List list = (List) redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 设置要监控 key1
                operations.watch(key("transaction:key1"));
                // 开启事务，在 exec 执行命令前，全部都只是进到队列
                operations.multi();
                /**
                 * 此处 increment 会报错（字符串类型不能加1），但是该报错不会影响后续命令的执行
                 * redis 事务跟数据库事务最大的不同之处
                 */
                // operations.opsForValue().increment(key("transaction:key1"), 1);

                operations.opsForValue().set(key("transaction:key2"), "val2");
                // 获取值将为null，因为此时 redis 不会立即执行，而是将命令放在一个队列
                Object val2 = operations.opsForValue().get(key("transaction:key2"));
                System.out.println("命令只是进入队列，所以 value 为 null：【" + val2 + "】");

                // 执行exec命令，将先判断 key1 是否在监控后被修改过，如果是则不执行事务，否则才执行事务
                // 如果在 exec处打断点，并使用客户端修改redis中 key1 的值，则事务不会执行
                return operations.exec();
            }
        });
        System.out.println(list);
    }

    /**
     * 测试 redis 管道操作 （类似sql的批量提交） 可以提升大约 10 倍的速度
     * <p>默认情况下，redis客户端是一条条命令发送给Redis服务器的，性能显然不高，使用管道操作可以大幅提高执行多条命令的效率（减少多次网络传输）</p>
     */
    @Test
    public void testPipline() {
        // 先删除
        Set keys = redisTemplate.keys(key("pipline:key_*"));
        redisTemplate.delete(keys);

        Long start = System.currentTimeMillis();
        List list = (List) redisTemplate.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                int count = 10000;
                IntStream.range(0, count).forEach((i) -> {
                    operations.opsForValue().set(key("pipline:key_" + i), "value_" + i);

                    if (i == count) {
                        String value = (String) operations.opsForValue().get(key("pipline:key_" + i));
                        // 使用管道操作和事务类似，所有的命令也只是进入到队列并没有执行，所以此处返回值也是空
                        System.out.println("命令在队列，所以 value 为 null：【" + value + "】");
                    }
                });
                return null;
            }
        });
        Long end = System.currentTimeMillis();

        System.out.println("耗时： " + (end - start) + "毫秒。");
    }

    /**
     * 测试Redis的订阅/发布
     */
    @Test
    public void testPubSub() {
        String channel = "test:springboot2:topic";
        String message = "Xdsgfdfsd325345中文的kklk";
        redisTemplate.convertAndSend(channel, message);
        //RedisMessageListener 监听着消息，通过 RedisMessageConfiguration 自动装配相关的监听器，监听容器
    }

    /**
     * 测试 Redis Lua 脚本 (Redis 2.6+ 支持，且具备原子性，可增强运算能力)
     * <p>脚本不支持事务</p>
     * <p>
     *     有两种方式运行 Lua
     *     <li>直接发送到 Redis 执行</li>
     *     <li>Redis 把 Lua 缓存起来，返回一个 SHA1 的 32 编码，通过这个 SHA1 执行（可减少网络传输）</li>
     * </p>
     * @see org.springframework.data.redis.core.script.RedisScript
     */
    @Test
    public void testLua() {
        DefaultRedisScript<String> rs = new DefaultRedisScript<>();
        // 设置脚本
        rs.setScriptText("return 'Hello Springboot Data Redis..'");
        // 定义返回类型，注意：如果没有这个定义，Spring 不会返回结果
        rs.setResultType(String.class);
        RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
        // 执行 Lua 脚本（如果指定序列化器Serializer，则默认使用 RedisTemplate 提供的 valueSerializer）
        String str = (String) redisTemplate.execute(rs, stringSerializer, stringSerializer, null);
        System.out.println("Lua 执行结果：" + str);
        //TODO

        /* 测试带参数的 Lua */
        // 结果返回为long
        DefaultRedisScript rsLong = new DefaultRedisScript();
        rsLong.setResultType(Long.class);
        rsLong.setScriptText(getLuaScriptText());
        // 定义key参数
        List keyList = new ArrayList();
        keyList.add("test:springboot2:lua:key1");
        keyList.add("test:springboot2:lua:key2");
        String value1 = "val1";
        String value2 = "val2";
        // 传递两个序列化器参数值，其中给一个是key的序列化器，另一个是参数的序列化器
        Long result = (Long) redisTemplate.execute(rsLong, keyList, value1, value2);
        System.out.println("Lua 比较结果： " + result);

        /* 使用 Lua 的 SHA1 测试 */
        String luaSHA1 = rsLong.getSha1();
        System.out.println(luaSHA1);
    }

    private String getLuaScriptText() {
        return "redis.call('set', KEYS[1], ARGV[1]) " +
        "redis.call('set', KEYS[2], ARGV[2]) " +
        "local strl = redis.call('get', KEYS[1]) " +
        "local str2 = redis.call('get', KEYS[2]) " +
        "if strl == str2 then " +
        "return 1 " +
        "end " +
        "return 0";
    }

    private String key(String shortkey) {
        return KEY_PREFIX + shortkey;
    }
}
