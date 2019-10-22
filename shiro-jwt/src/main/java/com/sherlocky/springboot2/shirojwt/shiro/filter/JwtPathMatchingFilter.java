package com.sherlocky.springboot2.shirojwt.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.sherlocky.springboot2.shirojwt.constant.StatusCodeEnum;
import com.sherlocky.springboot2.shirojwt.domain.bo.ResponseBean;
import com.sherlocky.springboot2.shirojwt.service.AccountService;
import com.sherlocky.springboot2.shirojwt.shiro.constant.JwtConstants;
import com.sherlocky.springboot2.shirojwt.shiro.token.JwtAuthenticationToken;
import com.sherlocky.springboot2.shirojwt.shiro.util.JwtSessionCacheUtils;
import com.sherlocky.springboot2.shirojwt.shiro.util.ServletRequestUtils;
import com.sherlocky.springboot2.shirojwt.shiro.util.JwtUtils;
import com.sherlocky.springboot2.shirojwt.util.RequestResponseUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 支持restful url 的过滤链  JWT json web token 过滤器，无状态验证
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class JwtPathMatchingFilter extends AbstractPathMatchingFilter {
    private StringRedisTemplate redisTemplate;
    private AccountService accountService;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);

        //记录调用api日志到数据库
        /**TODO
         LogExeManager.getInstance().executeLogTask(LogTaskFactory.bussinssLog(WebUtils.toHttp(servletRequest).getHeader("account"),
         WebUtils.toHttp(servletRequest).getRequestURI(), WebUtils.toHttp(servletRequest).getMethod(), (short) 1, null));
         */
        boolean isJwtPost = (null != subject && !subject.isAuthenticated()) && isJwtSubmission(servletRequest);
        // 判断是否为JWT认证请求
        if (isJwtPost) {
            AuthenticationToken token = createJwtAuthenticationToken(servletRequest);
            try {
                subject.login(token);
                return this.checkRoles(subject, mappedValue);
            } catch (AuthenticationException e) {
                // 如果是JWT过期
                if (JwtConstants.TOKEN_EXPIRED.equals(e.getMessage())) {
                    // 这里初始方案先抛出令牌过期，之后设计为在Redis中查询当前subject对应令牌，其设置的过期时间是JWT的两倍，此作为JWT的refresh时间
                    // 当JWT的有效时间过期后，查询其refresh时间，refresh时间有效即重新派发新的JWT给客户端，
                    // refresh也过期则告知客户端JWT时间过期重新认证

                    // 当存储在redis的JWT没有过期，即refresh time 没有过期
                    String account = WebUtils.toHttp(servletRequest).getHeader("account");
                    String jwt = WebUtils.toHttp(servletRequest).getHeader("authorization");
                    String refreshJwt = JwtSessionCacheUtils.get(redisTemplate, account);
                    if (Objects.equals(jwt, refreshJwt)) {
                        // 重新申请新的JWT
                        // 根据subject获取其对应所拥有的角色(这里设计为角色对应资源，没有权限对应资源)
                        String newJwt = JwtUtils.issueJWT(account, accountService.loadAccountRole(account), null);
                        // 将签发的JWT存储到Redis
                        JwtSessionCacheUtils.set(redisTemplate, account, newJwt);
                        RequestResponseUtil.responseWrite(ResponseBean.ok(StatusCodeEnum.JWT_NEW).addData("jwt", newJwt), servletResponse);
                        return false;
                    } else {
                        // jwt时间失效过期,jwt refresh time失效 返回jwt过期客户端重新登录
                        ResponseBean message = ResponseBean.error(StatusCodeEnum.JWT_EXPIRED);
                        RequestResponseUtil.responseWrite(JSON.toJSONString(message), servletResponse);
                        return false;
                    }

                }
                // 其他的判断为JWT错误无效
                RequestResponseUtil.responseWrite(ResponseBean.error(StatusCodeEnum.JWT_ERROR), servletResponse);
                return false;

            } catch (Exception e) {
                // 其他错误
                log.error(ServletRequestUtils.host(servletRequest) + "--JWT认证失败", e);
                // 告知客户端JWT错误1005,需重新登录申请jwt
                RequestResponseUtil.responseWrite(ResponseBean.error(StatusCodeEnum.JWT_ERROR), servletResponse);
                return false;
            }
        } else {
            // 请求未携带jwt 判断为无效请求
            RequestResponseUtil.responseWrite(ResponseBean.error(StatusCodeEnum.INVALID_REQUEST), servletResponse);
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        // 未认证的情况上面已经处理  这里处理未授权
        if (subject != null && subject.isAuthenticated()) {
            //  已经认证但未授权的情况
            // 告知客户端JWT没有权限访问此资源
            RequestResponseUtil.responseWrite(ResponseBean.error(StatusCodeEnum.JWT_NO_PERMISSION), servletResponse);
        }
        // 过滤链终止
        return false;
    }

    private boolean isJwtSubmission(ServletRequest request) {
        String jwt = RequestResponseUtil.getHeader(request, "authorization");
        String account = RequestResponseUtil.getHeader(request, "account");
        return (request instanceof HttpServletRequest)
                && !StringUtils.isEmpty(jwt)
                && !StringUtils.isEmpty(account);
    }

    private AuthenticationToken createJwtAuthenticationToken(ServletRequest request) {
        Map<String, String> maps = RequestResponseUtil.getRequestHeaders(request);
        return new JwtAuthenticationToken(request.getRemoteAddr(), maps.get("deviceInfo"), maps.get("authorization"), maps.get("account"));
    }

    /**
     * 验证当前用户是否属于mappedValue任意一个角色
     *
     * @param subject
     * @param mappedValue
     * @return boolean
     */
    private boolean checkRoles(Subject subject, Object mappedValue) {
        String[] rolesArray = (String[]) mappedValue;
        return rolesArray == null || rolesArray.length == 0 || Stream.of(rolesArray).anyMatch(role -> subject.hasRole(role.trim()));
    }
}
