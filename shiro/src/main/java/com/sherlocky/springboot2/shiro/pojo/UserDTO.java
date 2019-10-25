package com.sherlocky.springboot2.shiro.pojo;

import com.google.common.base.Converter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * 用户数据传输对象
 * <p>对象之间转换参考：<a href="http://lrwinx.github.io/2017/03/04/细思极恐-你真的会写java吗/">细思极恐-你真的会写java吗</a></p>
 *
 * <p>还可以用 hibernate 提供的 jsr 303 实现，对DTO进行合法性的验证</p>
 *
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

    public UserPO convert2User(){
        UserDTOConvert userDTOConvert = new UserDTOConvert();
        // convert 最终会转化为调用 Converter#doForward
        UserPO convert = userDTOConvert.convert(this);
        return convert;
    }

    public UserDTO convert4(UserPO user){
        UserDTOConvert userDTOConvert = new UserDTOConvert();
        // reverse convert 最终会转化为调用 Converter#doBackward
        UserDTO convert = userDTOConvert.reverse().convert(user);
        return convert;
    }


    /**
     * 继承 {@link com.google.common.base.Converter} 实现
     */
    private static class UserDTOConvert extends Converter<UserDTO, UserPO> {
        @Override
        protected UserPO doForward(UserDTO userDTO) {
            UserPO user = new UserPO();
            BeanUtils.copyProperties(userDTO,user);
            return user;
        }

        @Override
        protected UserDTO doBackward(UserPO user) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user,userDTO);
            return userDTO;
        }

        /** 有的业务场景出参和入参的DTO对象是不同的，
         * 可以将逆向操作实现为无法调用的。
        @Override
        protected UserDTO doBackward(UserPO user) {
            throw new AssertionError("不支持逆向转化方法!");
        }
        */
    }
}
