package com.sherlocky.springboot2.shirojwt.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户数据传递对象
 * <p>正确定义的dto是：数据传输对象，Service或Manager向外传输的对象。</p>
 * @author: zhangcx
 * @date: 2019/10/22 16:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String uid;
    private String username;
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
