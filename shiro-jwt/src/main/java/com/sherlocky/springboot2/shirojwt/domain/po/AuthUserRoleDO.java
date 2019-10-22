package com.sherlocky.springboot2.shirojwt.domain.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户角色 - 对应数据库中结构
 */
@Data
@NoArgsConstructor
public class AuthUserRoleDO {
    private Integer id;
    private String userId;
    private Integer roleId;
    private Date createTime;
    private Date updateTime;
}