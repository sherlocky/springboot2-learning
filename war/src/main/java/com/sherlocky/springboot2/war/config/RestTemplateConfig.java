package com.sherlocky.springboot2.war.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Autowired
    private HttpPoolProperties httpPoolProperties;

    /**
     * RestTemplate 是 spring 支持的一个请求 http rest 服务的模板对象，性质上有点像jdbcTemplate
     * <p>
     * 底层还是使用的 {@link HttpClient} 发送请求的
     * </p>
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean
    public HttpClient httpClient() {
        // 实例化一个连接池管理器，设置最大连接数等
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(httpPoolProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpPoolProperties.getDefaultMaxPerRoute());
        connectionManager.setValidateAfterInactivity(httpPoolProperties.getValidateAfterInactivity());
        /**
         * 通过 RequestConfig 的 custom 方法来构造一个连接信息
         * 还可以设置 proxy，cookie 等属性
         */
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(httpPoolProperties.getSocketTimeout())
                .setConnectTimeout(httpPoolProperties.getConnectTimeout())
                .setConnectionRequestTimeout(httpPoolProperties.getConnectionRequestTimeout())
                .build();
        // 实例化连接池，设置连接池管理器。
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }
}
