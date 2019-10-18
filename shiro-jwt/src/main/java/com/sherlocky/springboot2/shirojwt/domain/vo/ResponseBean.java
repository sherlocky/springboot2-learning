package com.sherlocky.springboot2.shirojwt.domain.vo;

import com.sherlocky.springboot2.shirojwt.constant.StatusCodeEnum;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 前后端统一消息定义协议 Message  之后前后端数据交互都按照规定的类型进行交互
 * {
 * meta:{"code":code,“msg”:message}
 * data:{....}
 * }
 */
public class ResponseBean {

    /**
     * 消息头meta 存放状态信息 code message
     */
    private Map<String, Object> meta = new HashMap<String, Object>();
    /**
     * 消息内容  存储实体交互数据
     */
    private Map<String, Object> data = new HashMap<String, Object>();

    public Map<String, Object> getMeta() {
        return meta;
    }

    public ResponseBean setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public ResponseBean setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public ResponseBean addMeta(String key, Object object) {
        this.meta.put(key, object);
        return this;
    }

    public ResponseBean addData(String key, Object object) {
        this.data.put(key, object);
        return this;
    }

    public static ResponseBean ok(StatusCodeEnum statusCode) {
        ResponseBean rb = new ResponseBean();
        rb.addMeta("success", Boolean.TRUE)
                .addMeta("code", statusCode.getCode())
                .addMeta("msg", statusCode.getMsg())
                .addMeta("timestamp", new Timestamp(System.currentTimeMillis()));
        return rb;
    }

    public static ResponseBean error(StatusCodeEnum statusCode) {
        ResponseBean rb = new ResponseBean();
        rb.addMeta("success", Boolean.FALSE)
                .addMeta("code", statusCode.getCode())
                .addMeta("msg", statusCode.getMsg())
                .addMeta("timestamp", new Timestamp(System.currentTimeMillis()));
        return rb;
    }
}
