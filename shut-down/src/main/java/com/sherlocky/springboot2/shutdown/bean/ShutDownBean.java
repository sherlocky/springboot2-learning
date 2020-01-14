package com.sherlocky.springboot2.shutdown.bean;

import javax.annotation.PreDestroy;

/**
 * 处理 shutdown 前的一些操作
 * @author: zhangcx
 * @date: 2020/1/13 11:02
 * @since:
 */
public class ShutDownBean {
    @PreDestroy
    public void preDestroy() {
        System.out.println("ShutDownBean is destroyed~~~");
    }
}
