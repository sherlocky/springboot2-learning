package com.sherlocky.springboot2.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot 提供了运行时的应用监控和管理的功能
 * <p>启动后访问<a href="http://localhost:8080/actuator">http://localhost:8080/actuator</a></p>
 * @author Sherlock
 */
@RestController
@SpringBootApplication
public class Springboot2ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot2ActuatorApplication.class, args);
    }
}
