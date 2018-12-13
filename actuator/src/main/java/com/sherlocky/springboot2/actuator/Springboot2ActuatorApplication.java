package com.sherlocky.springboot2.actuator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

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

    /** 覆盖 SpringBoot reids 默认使用的序列化器（默认的为二进制，对可视化不友好） */
    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 设置值（value）的序列化采用 Jackson2JsonRedisSerializer
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // 设置值（key）的序列化采用 StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer()); //2

        template.afterPropertiesSet();
        return template;
    }
}
