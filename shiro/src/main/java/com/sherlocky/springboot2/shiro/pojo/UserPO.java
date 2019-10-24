package com.sherlocky.springboot2.shiro.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.Set;

/**
 * 用户持久化对象
 *
 * @author: zhangcx
 * @date: 2019/10/23 17:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPO {
    private Long uid;
    private String username;
    private String name;
    private String password;
    private String salt;
    @Builder.Default
    private Boolean valid = true;
    private Set<String> roles;

    /**
     * 密码加密方法
     * <p>密码加盐加密，和 {@link org.apache.shiro.authc.credential.HashedCredentialsMatcher} 对应 </p>
     *
     * <p>演示需要，暂时放到这个类里</p>
     * @param password
     * @param salt
     * @return
     */
    public static String passwordEncrypt(String password, String salt) {
        //加密方式
        String algorithmName = "MD5";
        //盐值
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        //加密次数
        int hashIterations = 6;
        SimpleHash result = new SimpleHash(algorithmName, password, byteSalt, hashIterations);
        //Md2Hash Md5Hash Sha1Hash Sha256Hash Sha384Hash Sha512Hash 最后都是调用SimpleHash加密
        //Md5Hash r = new Md5Hash(password,byteSalt,hashIterations);
        return result.toHex();
    }
}
