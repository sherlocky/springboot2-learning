package com.sherlocky.springboot2.shirojwt.service.impl;

import com.google.common.collect.Lists;
import com.sherlocky.springboot2.shirojwt.domain.po.AuthUserDO;
import com.sherlocky.springboot2.shirojwt.domain.po.AuthUserRoleDO;
import com.sherlocky.springboot2.shirojwt.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户 业务类
 * <p>暂时不读写数据库，模拟用户CURD操作</p>
 * TODO
 */
@Service
public class UserServiceImpl implements UserService {
    private AuthUserDO admin;
    @PostConstruct
    void init() {
        admin = AuthUserDO.builder()
                .uid("1")
                .username("admin")
                .salt("system")
                // MD5(admin+盐) 加密后的字符串
                .password("033766688d73f3611a3f3caff4fc45cf")
                // 6位 盐
                .realName("管理员")
                .build();
    }

    @Override
    public String loadAccountRole(String account) throws DataAccessException {
        return "system";
    }

    @Override
    public List<AuthUserDO> getUserList() throws DataAccessException {
        return Lists.newArrayList(admin);
    }

    @Override
    public List<AuthUserDO> getUserListByRoleId(Integer roleId) throws DataAccessException {
        // TODO
        return null;
    }

    /**
     * 保存用户 角色关联
     * @param uid
     * @param roleId
     * @return
     * @throws DataAccessException
     */
    @Override
    public boolean authorityUserRole(String uid, int roleId) throws DataAccessException {
        AuthUserRoleDO authUserRole = new AuthUserRoleDO();
        authUserRole.setRoleId(roleId);
        authUserRole.setUserId(uid);
        authUserRole.setCreateTime(new Date());
        authUserRole.setUpdateTime(new Date());
        // return authUserRoleMapper.insert(authUserRole) == 1? Boolean.TRUE :Boolean.FALSE;
        return true;
    }

    /**
     * 删除用户 角色关联
     * @param uid
     * @param roleId
     * @return
     * @throws DataAccessException
     */
    @Override
    public boolean deleteAuthorityUserRole(String uid, int roleId) throws DataAccessException {
        AuthUserRoleDO authUserRole = new AuthUserRoleDO();
        authUserRole.setUserId(uid);
        authUserRole.setRoleId(roleId);
        // return authUserRoleMapper.deleteByUniqueKey(authUserRole) == 1? Boolean.TRUE : Boolean.FALSE;
        return true;
    }


    @Override
    public AuthUserDO getUserByAccount(String account) throws DataAccessException {
        if (Objects.equals(admin.getUsername(), account)) {
            return admin;
        }
        return null;
        // return userMapper.selectByUniqueKey(account);
    }

    @Override
    public AuthUserDO getUserByUid(String uid) {
        if (Objects.equals(admin.getUsername(), uid)) {
            return admin;
        }
        return null;
    }

    @Override
    public List<AuthUserDO> getNotAuthorityUserListByRoleId(Integer roleId) throws DataAccessException {
        return Lists.newArrayList(admin);
        //return userMapper.selectUserListExtendByRoleId(roleId);
    }
}
