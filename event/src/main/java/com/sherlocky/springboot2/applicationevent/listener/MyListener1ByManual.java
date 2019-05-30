package com.sherlocky.springboot2.applicationevent.listener;

import com.sherlocky.springboot2.applicationevent.event.MyBlogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * SpringBoot 事件监听方法1：手动向ApplicationContext中添加监听器
 * <p>需要在启动类中，手动装载监听</p>
 * @author sherlocky
 */
public class MyListener1ByManual implements ApplicationListener<MyBlogEvent> {
    Logger logger = LoggerFactory.getLogger(MyListener1ByManual.class);

    @Override
    public void onApplicationEvent(MyBlogEvent event) {
        logger.info(String.format("%s监听到事件源：%s.", MyListener1ByManual.class.getName(), event.getSource()));
    }
}