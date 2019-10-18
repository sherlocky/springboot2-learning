package com.sherlocky.springboot2.shirojwt.shiro.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sherlocky.springboot2.shirojwt.domain.vo.JwtAccount;
import com.sherlocky.springboot2.shirojwt.shiro.constant.JwtConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT 工具类
 */
public class JwtUtils {
    /** 默认过期时间 5 hours（单位：秒） */
    private static final long DEFAULT_EXPIRED_TIME_SECONDS = 5 * 60 * 60L;
    private static final String DEFAULT_ISSUER = "token-server";
    //TODO 改成fastjson
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int COUNT_2 = 2;
    private static final CompressionCodecResolver codecResolver = new DefaultCompressionCodecResolver();

    private JwtUtils() {

    }

    /**
     * 默认的 jwt 签发方法
     * @param account
     * @param roles
     * @param permissions
     * @return
     */
    public static String issueJWT(String account, String roles, String permissions) {
        return JwtUtils.issueJWT(StringUtils.replace(UUID.randomUUID().toString(), "-", ""),
                account, DEFAULT_ISSUER, DEFAULT_EXPIRED_TIME_SECONDS,
                roles, permissions, SignatureAlgorithm.HS512);
    }

    /**
     * json web token 签发
     *
     * @param id          令牌ID
     * @param account     用户ID
     * @param issuer      签发人
     * @param period      有效时间(毫秒)
     * @param roles       访问主张-角色
     * @param permissions 访问主张-权限
     * @param algorithm   加密算法
     * @return java.lang.String
     */
    public static String issueJWT(String id, String account, String issuer, Long period, String roles, String permissions, SignatureAlgorithm algorithm) {
        // 当前时间戳
        Long currentTimeMillis = System.currentTimeMillis();
        // 秘钥
        byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(JwtConstants.SECRET_KEY);
        JwtBuilder jwtBuilder = Jwts.builder();
        if (!StringUtils.isEmpty(id)) {
            jwtBuilder.setId(id);
        }
        if (!StringUtils.isEmpty(account)) {
            jwtBuilder.setSubject(account);
        }
        if (!StringUtils.isEmpty(issuer)) {
            jwtBuilder.setIssuer(issuer);
        }
        // 设置签发时间
        jwtBuilder.setIssuedAt(new Date(currentTimeMillis));
        // 设置到期时间
        if (null != period) {
            jwtBuilder.setExpiration(new Date(currentTimeMillis + period * 1000));
        }
        if (!StringUtils.isEmpty(roles)) {
            jwtBuilder.claim("roles", roles);
        }
        if (!StringUtils.isEmpty(permissions)) {
            jwtBuilder.claim("perms", permissions);
        }
        // 压缩，可选GZIP
        jwtBuilder.compressWith(CompressionCodecs.DEFLATE);
        // 加密设置
        jwtBuilder.signWith(Keys.hmacShaKeyFor(secreKeyBytes), algorithm);
        return jwtBuilder.compact();
    }

    /**
     * 解析JWT的Payload
     */
    public static String parseJwtPayload(String jwt) {
        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;
        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        for (char c : jwt.toCharArray()) {
            if (c == '.') {
                CharSequence tokenSeq = io.jsonwebtoken.lang.Strings.clean(sb);
                String token = tokenSeq != null ? tokenSeq.toString() : null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (delimiterCount != COUNT_2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }
        if (base64UrlEncodedPayload == null) {
            throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
        }
        // =============== Header =================
        Header header = null;
        CompressionCodec compressionCodec = null;
        if (base64UrlEncodedHeader != null) {
            String origValue = new String(Decoders.BASE64URL.decode(base64UrlEncodedHeader), io.jsonwebtoken.lang.Strings.UTF_8);
            Map<String, Object> m = readValue(origValue);
            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader(m);
            }
            compressionCodec = codecResolver.resolveCompressionCodec(header);
        }
        // =============== Body =================
        byte[] payloadBytes = Decoders.BASE64URL.decode(base64UrlEncodedPayload);
        if (compressionCodec != null) {
            payloadBytes = compressionCodec.decompress(payloadBytes);
        }
        return new String(payloadBytes, io.jsonwebtoken.lang.Strings.UTF_8);
    }

    /**
     * 验签JWT
     *
     * @param jwt json web token
     */
    public static JwtAccount parseJwt(String jwt, String appKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(appKey))
                .parseClaimsJws(jwt)
                .getBody();
        JwtAccount jwtAccount = new JwtAccount();
        //令牌ID
        jwtAccount.setTokenId(claims.getId());
        // 客户标识
        jwtAccount.setAccount(claims.getSubject());
        // 签发者
        jwtAccount.setIssuer(claims.getIssuer());
        // 签发时间
        jwtAccount.setIssuedAt(claims.getIssuedAt());
        // 接收方
        jwtAccount.setAudience(claims.getAudience());
        // 访问主张-角色
        jwtAccount.setRoles(claims.get("roles", String.class));
        // 访问主张-权限
        jwtAccount.setPerms(claims.get("perms", String.class));
        return jwtAccount;
    }


    /**
     * description 从json数据中读取格式化map
     *
     * @param val 1
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readValue(String val) {
        try {
            return MAPPER.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }


}
