package com.sherlocky.springboot2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan({"com.sherlocky.springboot2.web.mvc","com.sherlocky.springboot2.web.flux"})
public class Springboot2Application {

    public static void main(String[] args) {
        SpringApplication.run(Springboot2Application.class, args);
    }
}
