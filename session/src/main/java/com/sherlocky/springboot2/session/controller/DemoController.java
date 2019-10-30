package com.sherlocky.springboot2.session.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试用
 * @author: zhangcx
 * @date: 2019/10/20 13:29
 */
@RestController
@RequestMapping("/")
public class DemoController {
    @GetMapping
    public Map<String, Object> demo() {
        return new HashMap() {{
            put("id", 0L);
            put("name", "demo");
            put("man", true);
            put("skill", new String[]{"linux", "java", "docker", "spring"});
            put("job", null);
        }};
    }
}
