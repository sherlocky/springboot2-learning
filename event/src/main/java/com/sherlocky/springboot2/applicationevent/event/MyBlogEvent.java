package com.sherlocky.springboot2.applicationevent.event;

import org.springframework.context.ApplicationEvent;

/**
 * 自定义一个事件 -- 博客事件
 * @author
 */
public class MyBlogEvent extends ApplicationEvent {
    public MyBlogEvent(Object source) {
        super(source);
    }
}