package com.sherlocky.springboot2.validation.domain;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 当用户对象属性越多时，校验的逻辑也越来越多
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class UserBO {
    @NotBlank(message = "用户名不能为空")
    private String name;
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    private String phone;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;
    /** 嵌套/级联校验 */
    @NotNull(message = "父母名字不能为空")
    @Valid
    private UserRelationBO relation;
}