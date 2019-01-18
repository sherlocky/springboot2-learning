package com.sherlocky.springboot2.redis;

import com.sherlocky.springboot2.redis.dao.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 测试 Spring data redis 的一些简单用法
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2RedisSimpleTests {
    // 添加一个统一的key前缀，测试方便
    private final String KEY_PREFIX = "test:springboot2:";

    // Spring Boot 已自动配置，可直接注入
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /** 查看源码可知，每次使用 RedisTemplate 都会从连接工厂获取可用连接
     * 执行命令后，再关闭连接，存在一定的资源浪费
     */
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    /**
     * redisTemplate 并不是ValueOperations的实现类（非同一种类型），此种情况注入时 Spring 会使用 editor 进行转换。
     * 此处就会加载 ValueOperationsEditor，XXXEditor 类必须实现 PropertyEditor 接口。
     */
    // 使用@Resource注解指定stringRedisTemplate，可注入基于字符串的简单属性操作方法
    @Resource(name="stringRedisTemplate")
    private ValueOperations<String,String> valOpsStr;
    // 使用@Resource注解指定redisTemplate，可注入基于对象的简单属性操作方法
    /**
     *
     * 使用 Settings -> Editor -> Inspections -> Spring -> Spring Core -> Code -> Autowiring for Bean Class
     *      勾去掉，可解决IDEA @Autowired 报错问题
     * // @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") 也可解决
     */
    @Resource(name="redisTemplate")
    private ValueOperations<Object, Object> valOps;

    @Test
    public void testRedisTemplate() {
        /* ValueOperations 接口 */
        valOpsStr.set(KEY_PREFIX + "key1", "189889");
        System.out.println(valOpsStr.get(KEY_PREFIX + "key1"));

        valOps.set(KEY_PREFIX + "key2", new User(123L, "张三"));
        User u = (User) valOps.get(KEY_PREFIX + "key2");
        System.out.println(u);

        redisTemplate.opsForHash().put(KEY_PREFIX + "hash1", "field1", "asdasdasd");
        System.out.println(redisTemplate.opsForHash().entries(KEY_PREFIX + "hash1"));

        stringRedisTemplate.opsForHash().put(KEY_PREFIX + "hash2" , "field2","第三方斯蒂芬撒");
        System.out.println(stringRedisTemplate.opsForHash().entries(KEY_PREFIX + "hash2"));

        /* BoundXXXOperations 接口： 可以连续多次操作一个key */
        // 绑定散列操作的 key ， 这样可以连续对同一个散列数据类型进行操作
        BoundHashOperations hashOps = stringRedisTemplate.boundHashOps(KEY_PREFIX + "boundhash");
        // 新增两个字段
        hashOps.put("f1", "v1");
        hashOps.put("f2", "v2");
        System.out.println(hashOps.entries());
        // 删除一个字段
        hashOps.delete("f1", "v1") ;
        System.out.println(hashOps.entries());
    }

    // 获取底层 Jedis 连接
    @Test
    public void testJedisMethods() {
        Jedis jedis = (Jedis) stringRedisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        jedis.set(KEY_PREFIX + "int1", "0");
        // 减1 操作，这个命令 RedisTemplate 不支持，所以先获取底层的连接再操作
        jedis.decr(KEY_PREFIX + "int1");
        System.out.println(jedis.get(KEY_PREFIX + "int1"));
    }

    // 可以连续多次操作一个key(本质上还是 Operations，每次执行命令都是重新获取连接)
    @Test
    public void testBoundListOperations() {
        // 创建两个列表
        // 从左边放，链表中从左到右顺序为 v9 v7 v5 v3 v1
        stringRedisTemplate.opsForList().trim(KEY_PREFIX + "list1", 1, 0); // 先清空，trim 只保留指定区间内的元素（1比0大，则会清空）
        stringRedisTemplate.opsForList().leftPushAll(KEY_PREFIX + "list1", "v1", "v3", "v5", "v7", "v9");
        // 从右边放，链表中从左到右顺序为 v0 v2 v4 v6 v8
        stringRedisTemplate.opsForList().trim(KEY_PREFIX + "list2", 1, 0); // 先清空
        stringRedisTemplate.opsForList().rightPushAll(KEY_PREFIX + "list2", "v0", "v2", "v4", "v6", "v8");
        System.out.println(stringRedisTemplate.opsForList().range(KEY_PREFIX + "list1", 0, -1)); // 下标 -1 可表示最后一个元素
        System.out.println(stringRedisTemplate.opsForList().range(KEY_PREFIX + "list2", 0, -1));

        // 绑定 list2 链表操作
        BoundListOperations listOps = stringRedisTemplate.boundListOps(KEY_PREFIX + "list2");
        // 从右边弹出一个元素
        Object o1 = listOps.rightPop();
        listOps.getOperations().getClientList();
        // 获取定位元素，从0开始，应该是值 v4
        Object o2 = listOps.index(1);
        // 从左边插入链表
        listOps.leftPush("v-2");
        // 求链表长度: 5
        long size = listOps.size();
        System.out.println("list 长度： " + size);
        // 求链表下标区间成员
        System.out.println("list 元素s： ");
        List vList = listOps.range(0, -1); // 下标 -1 可表示最后一个元素
        System.out.println(vList);
    }

    @Test
    public void testBoundSetOperations() {
        // 创建两个集合
        // v1 v2 v3 v4 v5(集合不允许重复)
        stringRedisTemplate.delete(KEY_PREFIX + "set1"); // 先清空
        stringRedisTemplate.opsForSet().add(KEY_PREFIX + "set1", "v1", "v1", "v2", "v3", "v4", "v5");
        // v0 v2 v4 v6 v8
        stringRedisTemplate.delete(KEY_PREFIX + "set2"); // 先清空
        stringRedisTemplate.opsForSet().add(KEY_PREFIX + "set2", "v0", "v2", "v4", "v6", "v8");
        System.out.println(stringRedisTemplate.opsForSet().members(KEY_PREFIX + "set1"));
        System.out.println(stringRedisTemplate.opsForSet().members(KEY_PREFIX + "set2"));

        // 绑定 set1 集合操作
        BoundSetOperations setOps = stringRedisTemplate.boundSetOps(KEY_PREFIX + "set1");
        setOps.add("v6", "v7"); // 增加两个元素
        setOps.add("v1", "v7"); // 删除两个元素
        Set vSet = setOps.members();
        long size = vSet.size();
        System.out.println("集合个数： " + size);
        System.out.println(vSet);

        // 求交集
        Set inter = setOps.intersect(KEY_PREFIX + "set2");
        // 求交集，并保存到 test:springboot2:set_inter
        setOps.intersectAndStore(KEY_PREFIX + "set2", KEY_PREFIX + "set_inter");
        System.out.println("交集： " + stringRedisTemplate.opsForSet().members(KEY_PREFIX + "set_inter"));

        // 求差集
        setOps.diffAndStore(KEY_PREFIX + "set2", KEY_PREFIX + "set_diff");
        System.out.println("差集： " + stringRedisTemplate.opsForSet().members(KEY_PREFIX + "set_diff"));

        // 求并集
        setOps.unionAndStore(KEY_PREFIX + "set2", KEY_PREFIX + "set_union");
        System.out.println("并集： " + stringRedisTemplate.opsForSet().members(KEY_PREFIX + "set_union"));
    }

    /**
     * Redis 提供了有序集合（zset），有序集合与集合的差异并不大，它也是一种散列表存储的方式。
     * <p>同时它的有序性只是靠它在数据结构中增加一个属性 score （分数）得以支持。</p>
     * <p>为了支持这个变化， Spring 提供了 TypedTuple 接口，它定义了两个方法，并且 Spring 还提供了其默认的实现类DefaultTypedTuple。</p>
     * <li>getValue() ： value 是保存有序集合的值</li>
     * <li>getScore() ： score 是保存分数</li>
     */
    @Test
    public void testBoundZsetOperations() {
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
        IntStream.range(0, 10).forEach((i) -> {
            // 分数
            double score = i * 0.1;
            // 创建一个 TypedTuple 对象，存入值和分数
            typedTupleSet.add(new DefaultTypedTuple<String>("value " + i, score));
        });
        // 往有序集合插入元素
        stringRedisTemplate.opsForZSet().add(KEY_PREFIX + "zset", typedTupleSet);
        // 绑定有序集合
        BoundZSetOperations zsetOps = stringRedisTemplate.boundZSetOps(KEY_PREFIX + "zset");
        // 增加一个元素
        zsetOps.add("value X", 1.5);
        // 在下标区间下，按分数排序，获取有序集合（只有 value）
        Set<String> setRange = zsetOps.range(1, 6);
        System.out.println(setRange);
        // 在分数区间下，按分数排序，获取有序集合（只有 value）
        Set<String> scoreRange = zsetOps.rangeByScore(0.2, 0.6);
        System.out.println(scoreRange);

        // 定义值范围
        RedisZSetCommands.Range r = new RedisZSetCommands.Range();
        r.gt("value 1"); // 大于，还有 gte 大于等于
        r.lt("value 5");
        // 按值排序（字符串排序）
        Set<String> setLex = zsetOps.rangeByLex(r);
        System.out.println(scoreRange);

        // 删除元素
        zsetOps.remove("value 2", "value 9");
        // 求分数
        zsetOps.score("value 8");
        // 在下标区间下，按分数排序，获取有序集合（同时返回 value 和 score）
        Set<ZSetOperations.TypedTuple<String>> setRangeWithScore = zsetOps.rangeWithScores(1, 6);
        setRangeWithScore.forEach((typedTuple) -> {
            System.out.print(typedTuple.getValue() + "=" +  typedTuple.getScore() + ", ");
        });
        // 在分数区间下，按分数排序，获取有序集合（同时返回 value 和 score）
        Set<ZSetOperations.TypedTuple<String>> scoreRangeWithScore = zsetOps.rangeByScoreWithScores(0.2, 0.6);
        scoreRangeWithScore.forEach((typedTuple) -> {
            System.out.print(typedTuple.getValue() + "=" +  typedTuple.getScore() + ", ");
        });

        // 按从大到小获取（倒序）
        Set<String> setRangeReverse = zsetOps.reverseRange(1, 6);
        System.out.println(setRangeReverse);
    }

    /**
     * <p>查看源码可知，每次使用 RedisTemplate 都会从连接工厂获取可用连接，执行命令后，再关闭连接，存在一定的资源浪费。</p>
     * SessionCallback 接口和 RedisCallback 接口的作用是让RedisTemplate 进行回调，通过它们可以在同一条连接下执行多个 Redis 命令。
     * <li> 其中 SessionCallback 提供了良好的封装，对于开发者比较友好，因此在实际的开发中应该优先选择使用它；</li>
     * <li> 相对而言 RedisCallback 接口比较底层，需要处理的内容也比较多，可读性较差，所以在非必要 的时候尽量不选择使用它。</li>
     */
    @Test
    public void testRedisCallback() {
        // 使用lambda表达式实现匿名内部类（由于execute方法有重载，此处出现了 Ambiguous method call.故在括号中指定了使用的接口类型。）
        redisTemplate.execute((RedisCallback) (redisConnection) -> {
            redisConnection.set((KEY_PREFIX + "callback:key1").getBytes(), "value1".getBytes());
            redisConnection.hSet((KEY_PREFIX + "callback:hash1").getBytes(), "filed1".getBytes(), "hValue1".getBytes());
            System.out.println(new String(redisConnection.get((KEY_PREFIX + "callback:key1").getBytes())));
            Map<byte[], byte[]> hData = redisConnection.hGetAll((KEY_PREFIX + "callback:hash1").getBytes());
            hData.forEach((k, v) -> {
                System.out.println(new String(k) + "=" + new String(v));
            });
            return null;
        });
    }

    /**
     * SessionCallback 提供了良好的封装，对于开发者比较友好，因此在实际的开发中应该优先选择使用它
     */
    @Test
    public void testSessionCallback() {
        useSessionCallback();
        // useSessionCallbackByLambda();
    }

    private void useSessionCallback() {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.opsForValue().set(KEY_PREFIX + "callback:key2", "value2");
                redisOperations.opsForHash().put(KEY_PREFIX + "callback:hash2", "filed2", "hValue2");
                System.out.println(redisOperations.opsForValue().get(KEY_PREFIX + "callback:key2"));
                System.out.println(redisOperations.opsForHash().entries(KEY_PREFIX + "callback:hash2"));
                return null;
            }
        });
    }

    /**
     * 还可以用lambda 形式实现（IDEA下报一个类型应用的错，但可以运行）
     */
    /*
    private void useSessionCallbackByLambda() {
        redisTemplate.execute((SessionCallback) (redisOperations) -> {
            redisOperations.opsForValue().set(KEY_PREFIX + "callback:key2", "value2");
            redisOperations.opsForHash().put(KEY_PREFIX + "callback:hash2", "filed2", "hValue2");
            System.out.println(redisOperations.opsForValue().get(KEY_PREFIX + "callback:key2"));
            System.out.println(redisOperations.opsForHash().entries(KEY_PREFIX + "callback:hash2"));
            return null;
        });
    }
    */
}
