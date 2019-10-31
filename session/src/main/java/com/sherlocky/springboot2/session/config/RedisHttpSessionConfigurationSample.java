package com.sherlocky.springboot2.session.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * 【未使用，只作为记录】
 * Spring Session 的 @{@link EnableRedisHttpSession} 注解配置等价于 application 配置文件中的: <code>spring.session.store-type=redis</code>
 * <p>
 *   会创建一个实现了 {@link org.springframework.session.web.http.SessionRepositoryFilter} 的 springSessionRepositoryFilter，
 *   负责用 {@link javax.servlet.http.HttpSession} Redis 支持的实现代替 Tomcat 的 HttpSession。
 * </p>
 * 与security的配合：
 * <p>
 * 当 {@link org.springframework.security.web.context.SecurityContextPersistenceFilter}
 * 保存 {@link org.springframework.security.core.context.SecurityContext} 到 {@link javax.servlet.http.HttpSession} 时，
 * 然后将其持久化到redis。
 * </p>
 * <p>
 *     HttpSession 创建新内容时，Spring Session 将在浏览器中创建一个名为 SESSION 的 cookie 。该Cookie包含会话的ID。
 * </p>
 *
 * <p>可参考: https://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot-redis.html</p>
 * @author: zhangcx
 * @date: 2019/10/30 10:52
 * @since:
 */
// @EnableRedisHttpSession
public class RedisHttpSessionConfigurationSample {

    /**
     * 创建一个RedisConnectionFactory 通过Lettuce客户端将Spring Session连接到Redis Server的
     * @return
     */
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    /**
     * restful session 支持
     * <p>可以自定义Spring Session的 HttpSession 集成，以使用HTTP标头传达当前的会话信息，而不是cookie。</p>
     * <p>
     *     使用名为<code>X-Auth-Token</code>的 http header，其包含了sessionId
     * </p>
     * 可参考：https://docs.spring.io/spring-session/docs/current/reference/html5/guides/java-rest.html
     * @return
     */
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }
}
