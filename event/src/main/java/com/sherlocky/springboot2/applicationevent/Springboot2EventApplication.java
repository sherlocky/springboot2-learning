package com.sherlocky.springboot2.applicationevent;

import com.sherlocky.springboot2.applicationevent.event.MyBlogEvent;
import com.sherlocky.springboot2.applicationevent.listener.MyListener1ByManual;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Springboot2EventApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Springboot2EventApplication.class, args);
        /********************* 装载监听事件 *********************/
        // 方式1：手动装载监听
        context.addApplicationListener(new MyListener1ByManual());
        // 方式2：无需手动装载监听，@Component自动注入
        // 方式3：无需实现 ApplicationListener 接口，使用 @EventListener 方法注解

        /********************* 发布事件 *****************/
        context.publishEvent(new MyBlogEvent("测试博文事件~"));
    }
}
/**
 SpringBoot四种事件的实现方式监听是有序的

 com.sherlocky.springboot2.applicationevent.listeners.MyListener3ByProperties监听到事件源：测试博文事件~.
 com.sherlocky.springboot2.applicationevent.listeners.MyListener4ByEventListenerAnnotation监听到事件源：测试博文事件~.
 com.sherlocky.springboot2.applicationevent.listeners.MyListener2ByComponentAnnotation监听到事件源：测试博文事件~.
 com.sherlocky.springboot2.applicationevent.listeners.MyListener1ByManual监听到事件源：测试博文事件~.

 */