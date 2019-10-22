package com.sherlocky.springboot2.shirojwt.service;

import com.sherlocky.springboot2.shirojwt.domain.po.AuthUserDO;

import java.util.List;

/**
 * 用户 业务处理接口
 */
public interface UserService {

    String loadAccountRole(String account);

    List<AuthUserDO> getUserList();

    List<AuthUserDO> getUserListByRoleId(Integer roleId);

    boolean authorityUserRole(String appId, int roleId);

    boolean deleteAuthorityUserRole(String uid, int roleId);

    AuthUserDO getUserByAccount(String account);

    AuthUserDO getUserByUid(String uid);

    List<AuthUserDO> getNotAuthorityUserListByRoleId(Integer roleId);
}
