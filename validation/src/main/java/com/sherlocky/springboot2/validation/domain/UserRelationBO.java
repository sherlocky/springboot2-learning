package com.sherlocky.springboot2.validation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 用户关系 -- 测试嵌套/级联校验
 * @author: zhangcx
 * @date: 2019/11/5 15:48
 * @since:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationBO {
    @NotBlank(message = "父亲的姓名不能为空")
    private String fatherName;
    @NotBlank(message = "母亲的姓名不能为空")
    private String motherName;
}