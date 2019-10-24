package com.sherlocky.springboot2.shiro.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户数据传输对象
 * @author: zhangcx
 * @date: 2019/10/24 11:38
 */
@Data
@NoArgsConstructor
public class UserDTO {
    private Long uid;
    private String username;
    private String name;
    private String password;
    private Boolean rememberMe;
}
