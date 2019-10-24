package com.sherlocky.springboot2.shiro.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口响应实体
 * @author
 */
@Data
public class ResponseBean<T> implements Serializable {
    private String code;
    private String msg;
    private T data;
    private List<T> dataList;

    private ResponseBean<T> code(String code){
        this.code = code;
        return this;
    }

    private ResponseBean<T> msg(String msg){
        this.msg = msg;
        return this;
    }

    private ResponseBean<T> data(T data){
        this.data = data;
        return this;
    }

    private ResponseBean<T> dataList(List<T> dataList){
        this.dataList = dataList;
        return this;
    }

    public static <T>ResponseBean<T> success(){
        ResponseBean<T> dr = new ResponseBean<>();
        return dr.code("0");
    }

    public static <T>ResponseBean<T> success(T data){
        ResponseBean<T> dr = new ResponseBean<>();
        return dr.code("0").data(data);
    }

    public static <T>ResponseBean<T> success(List<T> dataList){
        ResponseBean<T> dr = new ResponseBean<>();
        return dr.code("0").dataList(dataList);
    }

    public static ResponseBean error(String message) {
        ResponseBean dr = new ResponseBean<>();
        return dr.code("-1").msg(message);
    }
}
