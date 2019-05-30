package com.sherlocky.springboot2.applicationevent;

import com.sherlocky.springboot2.applicationevent.service.MyEventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: zhangcx
 * @date: 2019/5/30 16:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot2EventApplicationTests {
    @Autowired
    private MyEventService service;

    @Test
    public void testPublishEvent() {
        String msg = "【单元测试】";
        service.publishEvent(msg);
    }
}
