package com.sherlocky.springboot2.applicationevent.service;

import com.sherlocky.springboot2.applicationevent.event.MyBlogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author: zhangcx
 * @date: 2019/5/30 15:59
 */
@Service
public class MyEventService {
    @Autowired
    private ApplicationContext publisher;

    public void publishEvent(String msg) {
        publisher.publishEvent(new MyBlogEvent("### 新发布事件 " + msg + " from SVC~"));
    }
}
