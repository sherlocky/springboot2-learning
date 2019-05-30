package com.sherlocky.springboot2.applicationevent.listener;

import com.sherlocky.springboot2.applicationevent.event.MyBlogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * SpringBoot 事件监听方法4：无需实现ApplicationListener接口，使用@EventListener装饰具体方法
 * @author sherlocky
 */
@Component
public class MyListener3ByEventListenerAnnotation {
    Logger logger = LoggerFactory.getLogger(MyListener3ByEventListenerAnnotation.class);

    @EventListener
    @Async // 异步监听，需要配合启动类上的@EnableAsync注解一起使用（会使用一个默认的线程池,可自定义线程池替代）
    public void listener(MyBlogEvent event) {
        logger.info(String.format("%s监听到事件源：%s.", MyListener3ByEventListenerAnnotation.class.getName(), event.getSource()));
    }
}