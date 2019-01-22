package com.sherlocky.springboot2.redis.dao;

import com.sherlocky.springboot2.redis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserDAO {
    // Spring Boot 已自动配置，可直接注入
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    // 使用@Resource注解指定stringRedisTemplate，可注入基于字符串的简单属性操作方法
    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;
    // 使用@Resource注解指定redisTemplate，可注入基于对象的简单属性操作方法
    @Resource(name = "redisTemplate")
    ValueOperations<Object, Object> valOps; //4

    public void stringRedisTemplateDemo() {
        valOpsStr.set("xx", "yy");
    }

    public String getString() {
        return valOpsStr.get("xx");
    }

    public void save(User u) {
        valOps.set(u.getId(), u);
    }

    public User getUser() {
        return (User) valOps.get("1");
    }
}
