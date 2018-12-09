package com.sherlocky.springboot2.starter.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自行实现一个类型安全的配置类
 * <p>支持嵌套（子类 public static class {}）<a href="https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor">官方文档地址</a></p>
 * <p>可以在字段上使用 @NestedConfigurationProperty 以在常规（非内部）类上实现嵌套</p>
 * @author: zhangcx
 * @date: 2018/12/8 21:51
 */
// IDEA下会报【Spring Boot Configuration Annotation Proessor not found in classpath】，是因为spring boot1.5+ @ConfigurationProperties 取消了 location 注解
@ConfigurationProperties(prefix="demo")
public class DemoServiceProperties {
    private  static final String DEFAULT_NAME = "DEMO";
    // 在application.proeprties中通过 demo.name=xx 来设置，若不设置，默认为demo.name=DEMO
    private String name = DEFAULT_NAME;
    private Host host;

    public static class Host {
        private String ip;
        private int port;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
