package com.sherlocky.springboot2.shirojwt.controller;

import com.sherlocky.common.util.Md5Utils;
import com.sherlocky.springboot2.shirojwt.constant.StatusCodeEnum;
import com.sherlocky.springboot2.shirojwt.domain.po.AuthUserDO;
import com.sherlocky.springboot2.shirojwt.domain.bo.ResponseBean;
import com.sherlocky.springboot2.shirojwt.domain.dto.UserDTO;
import com.sherlocky.springboot2.shirojwt.service.AccountService;
import com.sherlocky.springboot2.shirojwt.service.UserService;
import com.sherlocky.springboot2.shirojwt.shiro.util.DynamicKeyCacheUtils;
import com.sherlocky.springboot2.shirojwt.shiro.util.JwtSessionCacheUtils;
import com.sherlocky.springboot2.shirojwt.shiro.util.JwtUtils;
import com.sherlocky.springboot2.shirojwt.shiro.util.ServletRequestUtils;
import com.sherlocky.springboot2.shirojwt.util.AesUtils;
import com.sherlocky.springboot2.shirojwt.util.RequestResponseUtil;
import com.sherlocky.springboot2.shirojwt.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * post新增,get读取,put完整更新,patch部分更新,delete删除
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController extends BaseController {
    private static final String STR_USERNAME = "username";
    private static final String STR_REALNAME = "realName";
    private static final String STR_AVATAR = "avatar";
    private static final String STR_PHONE = "phone";
    private static final String STR_EMAIL = "email";
    private static final String STR_SEX = "sex";
    private static final String STR_WHERE = "createWhere";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    /**
     * 登录签发 JWT ,这里已经在 {@link com.sherlocky.springboot2.shirojwt.shiro.filter.PasswordAccessControlFilter} 进行了登录认证
     *
     * @param request  1
     * @param response 2
     * @return com.usthe.bootshiro.domain.vo.ResponseBean
     */
    @PostMapping("/login")
    public ResponseBean accountLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = RequestResponseUtil.getRequestBodyMap(request);
        String account = params.get("account");
        // 根据 account 获取其对应所拥有的角色(这里设计为角色对应资源，没有权限对应资源)
        String roles = accountService.loadAccountRole(account);
        // 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
        String jwt = JwtUtils.issueJWT(account, roles, null);
        // TODO
        // 将签发的JWT存储到Redis
        JwtSessionCacheUtils.set(redisTemplate, account, jwt);
        UserDTO user = new UserDTO();
        BeanUtils.copyProperties(userService.getUserByAccount(account), user);
        // LogExeManager.getInstance().executeLogTask(LogTaskFactory.loginLog(appId, IpUtil.getIpFromRequest(WebUtils.toHttp(request)), (short) 1, "登录成功"));
        return ResponseBean.ok(StatusCodeEnum.JWT_ISSUED_SUCCESS).addData("jwt", jwt).addData("user", user);
    }

    /**
     * 用户账号的注册
     *
     * @param request  1
     * @param response 2
     * @return com.usthe.bootshiro.domain.vo.ResponseBean
     */
    @PostMapping("/register")
    public ResponseBean accountRegister(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> params = RequestResponseUtil.getRequestBodyMap(request);
        AuthUserDO authUser = new AuthUserDO();
        String username = params.get("username");
        String password = params.get("password");
        String userKey = params.get("userKey");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            // 必须信息缺一不可,返回注册账号信息缺失
            return ResponseBean.error(StatusCodeEnum.ACCOUNT_INFO_LACK);
        }
        if (accountService.isAccountExistByUsername(username)) {
            // 账户已存在
            return ResponseBean.error(StatusCodeEnum.ACCOUNT_EXISTED);
        }

        authUser.setUid(UUIDUtils.get());

        // 从Redis取出密码传输加密解密秘钥
        String dynamicKey = DynamicKeyCacheUtils.get(redisTemplate, ServletRequestUtils.host(request), userKey);
        String realPassword = AesUtils.aesDecode(password, dynamicKey);
        // 6 位盐
        String salt = RandomStringUtils.randomAlphanumeric(6);
        // 存储到数据库的密码为 MD5(原密码+盐值)
        authUser.setPassword(Md5Utils.md5(realPassword + salt));
        authUser.setSalt(salt);
        authUser.setCreateTime(new Date());
        // TODO 感觉可优化
        if (!StringUtils.isEmpty(params.get(STR_USERNAME))) {
            authUser.setUsername(params.get(STR_USERNAME));
        }
        if (!StringUtils.isEmpty(params.get(STR_REALNAME))) {
            authUser.setRealName(params.get(STR_REALNAME));
        }
        if (!StringUtils.isEmpty(params.get(STR_AVATAR))) {
            authUser.setAvatar(params.get(STR_AVATAR));
        }
        if (!StringUtils.isEmpty(params.get(STR_PHONE))) {
            authUser.setPhone(params.get(STR_PHONE));
        }
        if (!StringUtils.isEmpty(params.get(STR_EMAIL))) {
            authUser.setEmail(params.get(STR_EMAIL));
        }
        if (!StringUtils.isEmpty(params.get(STR_SEX))) {
            authUser.setSex(Byte.valueOf(params.get(STR_SEX)));
        }
        if (!StringUtils.isEmpty(params.get(STR_WHERE))) {
            authUser.setCreateWhere(Byte.valueOf(params.get(STR_WHERE)));
        }
        authUser.setStatus((byte) 1);

        if (accountService.registerAccount(authUser)) {
            // LogExeManager.getInstance().executeLogTask(LogTaskFactory.registerLog(uid, IpUtil.getIpFromRequest(WebUtils.toHttp(request)), (short) 1, "注册成功"));
            return ResponseBean.ok(StatusCodeEnum.ACCOUNT_REGISTER_SUCCESS);
        } else {
            // LogExeManager.getInstance().executeLogTask(LogTaskFactory.registerLog(uid, IpUtil.getIpFromRequest(WebUtils.toHttp(request)), (short) 0, "注册失败"));
            return ResponseBean.ok(StatusCodeEnum.ACCOUNT_REGISTER_FAIL);
        }
    }

}
