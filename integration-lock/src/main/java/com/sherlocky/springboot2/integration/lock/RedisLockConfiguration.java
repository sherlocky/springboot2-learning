package com.sherlocky.springboot2.integration.lock;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.redis.connection.RedisConnectionFactory;
        import org.springframework.integration.redis.util.RedisLockRegistry;

@Configuration
public class RedisLockConfiguration {
    private static String REGISTRY_KEY = "spring-integration-lock";
    public static final String LOCK_KEY = "lock";

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, REGISTRY_KEY);
    }
}