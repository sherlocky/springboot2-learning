package com.sherlocky.springboot2.aop4logging.service;

import com.sherlocky.springboot2.aop4logging.aop.annotation.SherlockLogAnnotation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单Service示例，测试AOP Logging
 * @author: zhangcx
 * @date: 2019/1/25 10:42
 */
@Service
public class SampleService {

    /**
     * 简单的方法测试 Sherlock AOP Aspect
     * @param id
     * @param name
     * @param age
     * @param address
     * @return
     */
    @SherlockLogAnnotation("构建数据结果")
    public Map<String, String> buildData(String id, String name, int age, String address) {
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("age", String.valueOf(age));
        data.put("address", address);

        return data;
    }
}
