package com.sherlocky.springboot2.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SpringBoot 下war包运行
 * <p>参考：<a href="https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#howto-create-a-deployable-war-file">Spring Boot 2 Reference Guide</a></p>
 * @author: zhangcx
 * @date: 2019/7/10 17:13
 */
@SpringBootApplication
public class WarApplication extends SpringBootServletInitializer {

    /**
     * SpringBoot 项目打包war运行：
     *
     * 这样做可以利用Spring Framework的Servlet 3.0支持，并允许您在servlet容器启动时配置应用程序。
     *
     * build.gradle 提供的嵌入式servlet容器依赖项会生成一个可执行的war文件，其中提供的依赖项打包在lib-provided目录中。
     * 这意味着，除了可部署到servlet容器之外，还可以在命令行上使用java -jar运行应用程序。
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WarApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WarApplication.class, args);
    }
}
