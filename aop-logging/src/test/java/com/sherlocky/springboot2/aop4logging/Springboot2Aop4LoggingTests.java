package com.sherlocky.springboot2.aop4logging;

import com.sherlocky.springboot2.aop4logging.service.SampleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试 AOP Logging
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2Aop4LoggingTests {
    @Autowired
    private SampleService sampleService;

    @Test
    public void testLogAspect() {
        sampleService.buildData("1", "用户1", 18, "中国");
    }
}
