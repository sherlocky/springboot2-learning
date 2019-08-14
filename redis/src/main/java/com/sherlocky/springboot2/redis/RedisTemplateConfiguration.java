package com.app.document.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 装配 RedisTemplate Bean,替换为fastjson序列化器
 */
@Configuration
@ConditionalOnMissingBean(RedisTemplate.class)
@ConditionalOnClass(FastJsonRedisSerializer.class)
public class RedisTemplateConfiguration {
    /**
     * 使用fastjson序列化器
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisConnectionFactory);

        /**
         * 通常使用 GenericFastJsonRedisSerializer 即可满足大部分场景，
         * 如果你想定义特定类型专用的 RedisTemplate 可以使用 FastJsonRedisSerializer 来代替 GenericFastJsonRedisSerializer，配置是类似的。
         * GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
         */
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);

        /** 设置默认的Serialize，包含 keySerializer & valueSerializer */
        template.setDefaultSerializer(fastJsonRedisSerializer);
        // 单独设置keySerializer
        template.setKeySerializer(new StringRedisSerializer());
        // 单独设置valueSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        /** 设置 hash key 和 value 序列化模式 */
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());

        /** 必须执行这个函数,初始化RedisTemplate */
        template.afterPropertiesSet();
        return template;
    }

}
