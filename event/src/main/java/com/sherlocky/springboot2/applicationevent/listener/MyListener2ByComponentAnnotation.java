package com.sherlocky.springboot2.applicationevent.listener;

import com.sherlocky.springboot2.applicationevent.event.MyBlogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * SpringBoot 事件监听方法2：使用@Component注解将类装入Spring容器
 *
 * @author sherlocky
 */
@Component
public class MyListener2ByComponentAnnotation implements ApplicationListener<MyBlogEvent> {
    Logger logger = LoggerFactory.getLogger(MyListener2ByComponentAnnotation.class);

    @Override
    public void onApplicationEvent(MyBlogEvent event) {
        logger.info(String.format("%s监听到事件源：%s.", MyListener2ByComponentAnnotation.class.getName(), event.getSource()));
    }
}