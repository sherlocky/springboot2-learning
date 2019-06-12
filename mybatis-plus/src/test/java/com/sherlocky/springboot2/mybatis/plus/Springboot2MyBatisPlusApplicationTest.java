package com.sherlocky.springboot2.mybatis.plus;

import com.sherlocky.springboot2.mybatis.plus.entity.User;
import com.sherlocky.springboot2.mybatis.plus.mapper.UserMapper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 按照方法名 顺序执行单元测试
public class Springboot2MyBatisPlusApplicationTest {
    private Long userId = 1001L;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test001SelectAll() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void test002Insert() {
        System.out.println(("----- insert method test ------"));
        int result = userMapper.insert(new User(userId, "Sherlock", 18, "yyu@78778h.cc"));
        Assert.assertEquals(result, 1);

        User user = userMapper.selectById(userId);
        System.out.println(user);
    }

    @Test
    public void test003SelectById() {
        System.out.println(("----- selectById method test ------"));
        User user = userMapper.selectById(userId);
        Assert.assertNotNull(user);
        System.out.println(user);
    }


    @Test
    public void test004Update() {
        System.out.println(("----- update method test ------"));
        int result = userMapper.updateById(new User(userId, "Sherlock666", 16, "yyu888@78778h.cc"));
        Assert.assertEquals(result, 1);
        User user = userMapper.selectById(userId);
        System.out.println(user);
    }

    @Test
    public void test005Delete() {
        System.out.println(("----- delete method test ------"));
        int result = userMapper.deleteById(userId);
        Assert.assertEquals(result, 1);

        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void test006SelectObjects() {
        System.out.println(("----- selectObjects method test ------"));
        // 返回字段映射对象集合（只返回第一个字段的值）
        List<Object> objs = userMapper.selectObjs(null);
        if (objs != null && objs.size() > 0) {
            // Long
            System.out.println(objs.get(0).getClass());
        }
        System.out.println(objs);
    }
}