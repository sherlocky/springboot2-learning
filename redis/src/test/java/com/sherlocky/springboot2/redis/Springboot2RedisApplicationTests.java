package com.sherlocky.springboot2.redis;

import com.sherlocky.springboot2.redis.dao.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2RedisApplicationTests {

    // Spring Boot 已自动配置，可直接注入
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    // 使用@Resource注解指定stringRedisTemplate，可注入基于字符串的简单属性操作方法
    @Resource(name="stringRedisTemplate")
    ValueOperations<String,String> valOpsStr;
    // 使用@Resource注解指定redisTemplate，可注入基于对象的简单属性操作方法
    @Resource(name="redisTemplate")
    ValueOperations<Object, Object> valOps; //4

    @Test
    public void test() {
        valOpsStr.set("test:springboot2:key1", "189889");
        valOps.set("test:springboot2:key2", new User(123L, "张三"));
        redisTemplate.opsForHash().put("test:springboot2:hash1", "k1", "asdasdasd");

        System.out.println(valOpsStr.get("test:springboot2:key1"));
        User u = (User) valOps.get("test:springboot2:key2");
        System.out.println(u);

        System.out.println(redisTemplate.opsForHash().entries("test:springboot2:hash1"));
    }
}
