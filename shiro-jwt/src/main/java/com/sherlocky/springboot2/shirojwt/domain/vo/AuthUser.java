package com.sherlocky.springboot2.shirojwt.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AuthUser {
    private String uid;
    private String username;
    private String password;
    private String salt;
    private String realName;
    private String avatar;
    private String phone;
    private String email;
    private Byte sex;
    private Byte status;
    private Date createTime;
    private Date updateTime;
    private Byte createWhere;
}