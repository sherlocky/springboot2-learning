package com.sherlocky.springboot2.shirojwt.domain.bo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 账户
 */
@Data
@AllArgsConstructor
public class UserAccount implements Serializable {
    private static final long serialVersionUID = -365334057400418287L;
    /** 登录用户 */
    private String account;
    /** 登录密码 */
    private String password;
    private String salt;
}
