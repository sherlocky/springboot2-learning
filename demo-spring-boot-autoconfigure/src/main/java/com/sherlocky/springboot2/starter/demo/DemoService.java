package com.sherlocky.springboot2.starter.demo;

/**
 * 判断依据类
 * @author: zhangcx
 * @date: 2018/12/8 22:12
 */
public class DemoService {
    private String name;

    public String sayHello() {
        System.out.println("#########################################");
        System.out.println("Hello~" + name);
        System.out.println("#########################################");
        return "Hello~" + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
