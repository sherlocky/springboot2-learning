package com.sherlocky.springboot2.war.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "http-pool")
public class HttpPoolProperties {
    /**
     * 整个连接池的最大连接数
     */
    private Integer maxTotal;
    /**
     * 每个 route 默认的连接数
     */
    private Integer defaultMaxPerRoute;
    /**
     * 创建连接的最长时间
     * 连接上服务器(握手成功)的时间，超出则抛出 connect timeout
     */
    private Integer connectTimeout;
    /**
     * 从连接池中获取连接的超时时间
     * 超时间未拿到可用连接，会抛出 {@link org.apache.http.conn.ConnectionPoolTimeoutException} : Timeout waiting for connection from pool
     */
    private Integer connectionRequestTimeout;
    /**
     * 数据传输的最长时间，超过抛出 read timeout
     */
    private Integer socketTimeout;
    /**
     * 空闲永久连接检查间隔
     */
    private Integer validateAfterInactivity;

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getValidateAfterInactivity() {
        return validateAfterInactivity;
    }

    public void setValidateAfterInactivity(Integer validateAfterInactivity) {
        this.validateAfterInactivity = validateAfterInactivity;
    }
}
