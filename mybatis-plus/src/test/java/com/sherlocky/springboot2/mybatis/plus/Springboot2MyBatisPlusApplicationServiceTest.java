package com.sherlocky.springboot2.mybatis.plus;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sherlocky.springboot2.mybatis.plus.entity.User;
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
public class Springboot2MyBatisPlusApplicationServiceTest {
    private Long userId = 1001L;
    /**
     * <p>private UserService userService;</p>
     * <p>未启用事务之前可以使用 UserService(类)，
     * 启用事务后，由于@Transactional，Spring拦截并创建了一个代理类，因此Junit很难注入正确的mock </p>
     * <p>解决办法：</p>
     * <ul>
     *     <li>1.直接在Mapper层做测试，这些测试不受此影响。</li>
     *     <li>2.@Autowired时使用 IService< User >(接口)，而不是使用UserService(类)</li>
     * </ul>
     */
    @Autowired
    private IService<User> userService;

    @Test
    public void test001List() {
        System.out.println(("----- UserService :: list() method test ------"));
        List<User> userList = userService.list();
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void test002Save() {
        System.out.println(("----- UserService :: save() method test ------"));
        boolean result = userService.save(new User(userId, "Sherlock", 18, "yyu@78778h.cc"));
        Assert.assertTrue(result);

        User user = userService.getById(userId);
        System.out.println(user);
    }

    @Test
    public void test003GetById() {
        System.out.println(("----- UserService :: getById() method test ------"));
        User user = userService.getById(userId);
        Assert.assertNotNull(user);
        System.out.println(user);
    }


    @Test
    public void test004Update() {
        System.out.println(("----- UserService :: updateById() method test ------"));
        boolean result = userService.updateById(new User(userId, "Sherlock666", 16, "yyu888@78778h.cc"));
        Assert.assertTrue(result);
        User user = userService.getById(userId);
        System.out.println(user);
    }

    @Test
    public void test005Remove() {
        System.out.println(("----- UserService :: removeById() method test ------"));
        boolean result = userService.removeById(userId);
        Assert.assertTrue(result);

        List<User> userList = userService.list();
        userList.forEach(System.out::println);
    }

    @Test
    public void test006ListObjs() {
        System.out.println(("----- UserService :: listObjs() method test ------"));
        // 返回字段映射对象集合（只返回第一个字段的值）
        List<Object> objs = userService.listObjs();
        if (objs != null && objs.size() > 0) {
            // Long
            System.out.println(objs.get(0).getClass());
        }
        System.out.println(objs);
    }

    /**
     * 测试分页查询，需启用分页插件
     * @see com.sherlocky.springboot2.mybatis.plus.conf.MybatisPlusConfig#paginationInterceptor
     */
    @Test
    public void test007Page() {
        System.out.println(("----- UserService :: page() method test ------"));
        IPage<User> pu = new Page<>();
        pu.setSize(2); // 页大小 2
        pu.setTotal(5); // 总记录数 5
        pu.setCurrent(2); // 当前页号2，页号从1开始
        IPage<User> page = userService.page(pu);
        System.out.println(JSON.toJSONString(page, true));
        System.out.println("-----------------------------------------------");
        List<User> userList = page.getRecords();
        userList.forEach(System.out::println);
    }
}