package com.sherlocky.springboot2.redis;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试 Spring Cache Nacos config
 * @author: zhangcx
 * @date: 2020/4/13 18:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2NacosCacheTest {

    @Test
    public void test() {
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
