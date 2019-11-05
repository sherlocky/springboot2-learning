package com.sherlocky.springboot2.validation.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sherlocky.springboot2.validation.constant.ResponseCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回对象
 * @author: zhangcx
 * @date: 2019/11/5 15:24
 * @since:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse implements Serializable {

    /**
     * 状态值
     **/
    private int status;
    /**
     * 描述
     **/
    private String msg;
    /**
     * 数据
     **/
    private Object data;

    public ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static ServerResponse success() {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc());
    }

    public static ServerResponse illegalArgument(String msg) {
        return new ServerResponse(ResponseCode.ILLEGAL_ARGUMENT.getCode(), msg);
    }
}