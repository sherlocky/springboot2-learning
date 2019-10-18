package com.sherlocky.springboot2.shirojwt.shiro.rule;

import com.sherlocky.springboot2.shirojwt.shiro.constant.ShiroConstants;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Set;

/**
 * 角色权限资源 - 规则
 */
@Data
public class RolePermRule implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ANON_ROLE = "role_anon";

    /**
     * 资源URL
     */
    private String url;
    /**
     * 访问资源所需要的角色列表，多个列表用逗号间隔
     */
    private String needRoles;

    /**
     * 将url needRoles 转化成shiro可识别的过滤器链形式：url=jwt[角色1、角色2、角色n]
     * TODO ？ 没看明白
     * @return java.lang.StringBuilder
     */
    public String toFilterChain() {
        if (null == this.url || this.url.isEmpty()) {
            return null;
        }
        Set<String> roleSet = org.apache.shiro.util.StringUtils.splitToSet(
                this.getNeedRoles(), ShiroConstants.ROLES_SEPARATOR);

        StringBuilder stringBuilder = new StringBuilder();
        // 约定若role_anon角色拥有此uri资源的权限,则此uri资源直接访问不需要认证和权限
        if (!StringUtils.isEmpty(this.getNeedRoles()) && roleSet.contains(ANON_ROLE)) {
            stringBuilder.append(ShiroConstants.ANON);
        }
        //  其他自定义资源uri需通过jwt认证和角色认证
        if (!StringUtils.isEmpty(this.getNeedRoles()) && !roleSet.contains(ANON_ROLE)) {
            stringBuilder.append(String.format("jwt[%s]", this.getNeedRoles()));
        }
        return stringBuilder.length() > 0 ? stringBuilder.toString() : null;
    }
}
