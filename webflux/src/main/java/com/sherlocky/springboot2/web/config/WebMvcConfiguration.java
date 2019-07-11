package com.sherlocky.springboot2.web.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * configureMessageConverters 和 extendMessageConverters 只需重写一个即可。
     *
     * @param converters
     */
    //@Override
    public void _configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // fastjson 消息转换对象
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        /**
         * 自定义配置...如编码字符集，日期格式等
         */
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(Charset.forName("UTF-8"));
        converter.setFastJsonConfig(config);

        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(list);

        /** 添加到 converters*/
        converters.add(converter);

        /**
         * HttpMessageConverter，用于读或写请求或响应的主体。如果没有添加转换器，会使用注册的转换器的默认列表。
         * 在转换器列表convers中添加了自定义的fastjson转换器后，会关闭默认的转换器注册,导致其他类型请求（如：text）无法转换。
         * 此时可以手动添加默认的其他类型转换器（或者改为覆盖extendMessageConverters方法）：
         * 比如：支持text请求类型转换为string
         */
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        converters.add(stringConverter);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // fastjson 消息转换对象
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        /**
         * 自定义配置...如编码字符集，日期格式等
         */
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(Charset.forName("UTF-8"));
        converter.setFastJsonConfig(config);

        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(list);

        /**
         * 添加到 converters
         * 如果是直接添加，会排在默认的 MappingJackson 转换器后面，导致fastjson失效。
         * 方法1.可以遍历转换器列表，替换 MappingJackson 为 fastjson转换器
         * 方法2.或者直接在转换器列表首部加入fastjson转换器？（未验证）
         */
        // converters.add(0, converter);
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                converters.set(i, converter);
            }
        }
    }
}