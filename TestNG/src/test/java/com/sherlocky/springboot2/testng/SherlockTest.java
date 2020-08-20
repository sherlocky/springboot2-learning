package com.sherlocky.springboot2.testng;

import com.alibaba.fastjson.JSON;
import com.sherlocky.springboot2.testng.listener.ExtentTestNGIReporterListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Objects;

/**
 * TestNG 测试
 *
 * 支持两种执行方式，第一种是用注解像 Junit 直接点方法名 run 执行
 *
 * 第二种配置 xml 文件的方式，参见 TestNG.xml，直接右键 run 就运行了
 */
@Slf4j
@Listeners({ExtentTestNGIReporterListener.class})
@SpringBootTest(classes = Springboot2TestngReportApplication.class)
public class SherlockTest extends AbstractTestNGSpringContextTests {
    private int i ;


    /**
     * before/after 相关开头的TestNG注解可以在 @Test 方法执行前后 进行执行前准备和执行后收尾
     *
     * 感觉有点奇怪，BeforeClass注解的方法也必须配置个group，在group开启run的情况下才会执行。。
     *
     * 但是直接使用类运行时，不受group的约束，没有启用也会执行
     */
    @BeforeClass(groups = "before")
    public void beforeTest(){
        i = 10;
        System.out.println("beforeTest~~" + i);
    }

    /**
     * 在 group two结束后运行
     */
    @AfterGroups(groups = {"two"})
    public void afterTest(){
        i = -1;
        System.out.println("afterGroupTwoTest~~" + i);
    }


    /**
     * @DataProvider 注解为其他测试方法提供参数
     */
    @DataProvider(name = "paramDataProvider")
    public Object[][] paramDataProvider() {
        User user1 = new User();
        user1.setUserId(1);
        user1.setUserName("Sherlock1");
        User user2 = new User();
        user2.setUserId(2);
        user2.setUserName("Sherlock2");
        // 二维数组，相当于循环执行测试方法
        return new Object[][]{{1, user1}, {2, user2}};
    }

    @Test(dataProvider = "paramDataProvider", groups = "one")
    public void groupOneTestQueryUser(Integer id, User user) {
        if (id == 2) {
            throw new RuntimeException("$$$ Error~");
        }
        log.info("id：{}，user: {}", id, JSON.toJSONString(user));
        Assert.assertTrue(Objects.nonNull(user));
    }

    /**
     * priority 标识优先运行数字小的测试方法
     */
    @Test(groups = "two", priority = 3)
    public void groupTwoTestSample1() {
        log.info("我是测试方法1~");
    }

    @Test(groups = "two", priority = 2)
    public void groupTwoTestSample2() {
        log.info("我是测试方法2~");
    }

    @Test(groups = "two", priority = 1)
    public void groupTwoTestSample3() {
        log.info("我是测试方法3~");
    }

    @Test(groups = { "one", "two"}, priority = 0)
    public void groupOneAndTwoTestSample() {
        log.info("我也是测试方法~");
    }

    /**
     * 超时测试
     * 给测试方法一个超时时间，如果实际执行时间超过设定的超时时间，用例将不通过
     * @throws InterruptedException
     */
    @Test(timeOut = 5000)
    public void timeOutTest() throws InterruptedException {
        Thread.sleep(6000);
    }
    @Test
    public void token() {
        System.out.println("get token");
    }

    /**
     * 依赖测试
     */
    @Test(dependsOnMethods= {"token"})
    public void getUser() {
        System.out.println("this is test getUser");
    }

    @Data
    class User {
        private Integer userId;
        private String userName;
    }
}
