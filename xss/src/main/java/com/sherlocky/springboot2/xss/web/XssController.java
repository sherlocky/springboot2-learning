package com.sherlocky.springboot2.xss.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhangcx
 */
@RestController
@RequestMapping("/")
public class XssController {

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

    @GetMapping("/text")
    public String text() {
        return "demo";
    }
}
