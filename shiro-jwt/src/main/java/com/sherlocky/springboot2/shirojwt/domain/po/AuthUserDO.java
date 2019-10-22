package com.sherlocky.springboot2.shirojwt.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 对应数据库中结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserDO {
    /** uid 最好用Long，用专门的生成器实现，此处为了演示方便，使用String*/
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