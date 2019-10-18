package com.sherlocky.springboot2.shirojwt.service;

import com.sherlocky.springboot2.shirojwt.domain.vo.AuthUser;

import java.util.List;

/**
 * 用户 业务处理接口
 */
public interface UserService {

    String loadAccountRole(String appId);

    List<AuthUser> getUserList();

    List<AuthUser> getUserListByRoleId(Integer roleId);

    boolean authorityUserRole(String appId, int roleId);

    boolean deleteAuthorityUserRole(String uid, int roleId);

    AuthUser getUserByAccount(String account);

    List<AuthUser> getNotAuthorityUserListByRoleId(Integer roleId);
}
