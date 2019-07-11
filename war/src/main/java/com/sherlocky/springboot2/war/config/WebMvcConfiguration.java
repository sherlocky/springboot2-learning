package com.sherlocky.springboot2.war.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

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