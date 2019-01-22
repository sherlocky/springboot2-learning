package com.sherlocky.springboot2.redis;

import com.alibaba.fastjson.JSON;
import com.sherlocky.springboot2.redis.pojo.User;
import com.sherlocky.springboot2.redis.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试 Spring Cache Redis
 * <p> 继承关系： CacheManager -> AbstractCacheManager -> AbstractTransactionSupportingCacheManager -> RedisCacheManager</p>
 * @author: zhangcx
 * @date: 2019/1/18 15:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2RedisCacheTests {
    @Autowired
    private UserService userService;

    @Test
    public void testUserService() {
        println("获取id为1的用户：");
        beautiful(userService.getUserById(1L));
        println("【模拟的数据库中数据】为：");
        beautiful(userService.getSimulationUsersInfo());
        println("创建id为1的用户：");
        beautiful(userService.createUser(new User(1L, "用户_no_1")));
        println("【模拟的数据库中数据】为：");
        beautiful(userService.getSimulationUsersInfo());
        println("再次获取id为1的用户：");
        beautiful(userService.getUserById(1L));
        println("【模拟的数据库中数据】为：");
        beautiful(userService.getSimulationUsersInfo());
        println("更新用户1的姓名：");
        beautiful(userService.updateUserName(1L, "用户_no_1_new"));
        println("【模拟的数据库中数据】为：");
        beautiful(userService.getSimulationUsersInfo());
        println("删除用户1：");
        beautiful(userService.deleteUser(1L));
        println("【模拟的数据库中数据】为：");
        beautiful(userService.getSimulationUsersInfo());
    }

    public void beautiful(Object obj) {
        if (obj == null) {
            println("null");
            return;
        }
        if (obj instanceof String) {
            // println(obj);
            println(JSON.toJSONString(JSON.parseObject((String) obj), true));
            return;
        }
        // println(JSON.toJSONString(obj));
        println(JSON.toJSONString(obj, true));
    }

    public void println(Object obj) {
        System.out.println(obj);
    }
}
