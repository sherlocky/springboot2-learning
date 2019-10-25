package com.sherlocky.springboot2.admin_server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot admin server 示例
 *
 * <p>官方仓库：https://github.com/codecentric/spring-boot-admin</p>
 * <p>官方文档：https://codecentric.github.io/spring-boot-admin/2.1.6/</p>
 *
 * <p>如果想以war包形式运行，可参考: <a href="https://github.com/codecentric/spring-boot-admin/tree/master/spring-boot-admin-samples/spring-boot-admin-sample-war/">spring-boot-admin-sample-war</a></p>
 *
 * @author Administrator
 */
@SpringBootApplication
@EnableAdminServer
public class Springboot2AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot2AdminServerApplication.class, args);
    }
}
