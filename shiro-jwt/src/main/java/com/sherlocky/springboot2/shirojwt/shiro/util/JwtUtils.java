package com.sherlocky.springboot2.shirojwt.shiro.util;

import com.alibaba.fastjson.JSON;
import com.sherlocky.common.util.CryptoUtils;
import com.sherlocky.springboot2.shirojwt.domain.bo.JwtAccount;
import com.sherlocky.springboot2.shirojwt.shiro.constant.JwtConstants;
import com.sherlocky.springboot2.shirojwt.util.UUIDUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类 【JSON Web Signature(JWS)实现】
 *
 * <p>JSON Web Token（JWT）是一个非常轻巧的规范。这个规范允许我们使用JWT在两个组织之间传递安全可靠的信息。</p>
 *
 * <p>是自包含的令牌,自包含即整个令牌已经包含自己的角色，权限，用户信息等各种认证一个用户的必要信息，
 *  这样就不用后端根据用户标识再去数据库查询对应用户的角色权限等。</p>
 *
 * <p>JWT并不等于JWS，JSON Web Signature(JWS)只是JWT的一种实现，除了JWS外，JWE(JSON Web Encryption)也是JWT的一种实现。</p>
 *
 * <p>JSON Web Signature(JWS)包含头信息,载荷信息，签名信息三个部分（每个部分都是 Base64URL 编码的）:</p>
 * <code>
     Header //头信息
     {
     "alg": "HS256",  //摘要算法
     "typ": "JWT"     //token类型
     }
     payload //载荷信息
     {
     "sub": "1234567890", //用户标识,subject
     "name": "John Doe",  //用户名
     "exp": "Mon Nov 13 15:28:41 CST 2018" //有效期
     }
     verify signature //签名信息
     HMACSHA256(
     base64UrlEncode(header) + "." +
     base64UrlEncode(payload),
     your-256-bit-secret
     )
     * </code>
 */
@Slf4j
public class JwtUtils {
    public static final String DEFAULT_SECRET_KEY = "%3Dk^dZ4Wf8Toz*xXpWQwgOL";
    /** 默认过期时间 5 hours（单位：秒） */
    private static final long DEFAULT_EXPIRED_TIME_SECONDS = 5 * 60 * 60L;
    private static final String DEFAULT_ISSUER = "sherlocky-jwt";
    /** 默认算法使用：HmacSHA256 */
    private static final SignatureAlgorithm DEFAULT_ALGORITHM = SignatureAlgorithm.HS256;
    // jwt各部分分隔符
    private static final String JWT_PARTS_SEPARATOR = ".";
    private static final int JWT_PARTS_COUNT = 3;
    /** 压缩编解码器 解析 */
    private static final CompressionCodecResolver COMPRESSION_CODEC_RESOLVER = new DefaultCompressionCodecResolver();

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
        return JwtUtils.issueJWT(UUIDUtils.get(),
                account, DEFAULT_ISSUER, DEFAULT_EXPIRED_TIME_SECONDS,
                roles, permissions, DEFAULT_ALGORITHM);
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
     * @param algorithm   指定签名的时候使用的签名算法（也就是header那部分）
     * @return java.lang.String
     */
    public static String issueJWT(String id, String account, String issuer, Long period, String roles, String permissions, SignatureAlgorithm algorithm) {
        /** 为payload添加各种标准声明和私有声明 */
        // new一个JwtBuilder，设置jwt的body
        JwtBuilder jwtBuilder = Jwts.builder();
        // 设置 jti (JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
        if (!StringUtils.isEmpty(id)) {
            jwtBuilder.setId(id);
        }
        if (!StringUtils.isEmpty(account)) {
            jwtBuilder.setSubject(account);
        }
        if (!StringUtils.isEmpty(issuer)) {
            jwtBuilder.setIssuer(issuer);
        }
        // 当前时间戳
        Long currentTimeMillis = System.currentTimeMillis();
        // 设置签发时间
        jwtBuilder.setIssuedAt(new Date(currentTimeMillis));
        // 设置到期时间
        if (null != period) {
            jwtBuilder.setExpiration(new Date(currentTimeMillis + period * 1000));
        }
        // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
        if (!StringUtils.isEmpty(roles)) {
            jwtBuilder.claim(JwtConstants.CLAIMS_ROLES, roles);
        }
        if (!StringUtils.isEmpty(permissions)) {
            jwtBuilder.claim(JwtConstants.CLAIMS_PERMISSIONS, permissions);
        }
        // 使用GZIP压缩
        jwtBuilder.compressWith(CompressionCodecs.GZIP);
        // 设置签名使用的签名算法和签名使用的秘钥
        jwtBuilder.signWith(key4Issue(algorithm), algorithm);
        return jwtBuilder.compact();
    }

    /**
     * 签发JWT使用的key
     * <p>由字符串生成加密key</p>
     * @return
     */
    private static Key key4Issue(SignatureAlgorithm algorithm) {
        // TODO hmacShaKeyFor 和 secretKeyFor 什么区别

        try {
            // 是HAMC 算法（密钥加密）时
            return Keys.secretKeyFor(algorithm);
        } catch (IllegalArgumentException e) {
            log.info("$$$ 未采用 HAMC 算法。");
        }
        // 未采用 HMAC 算法时，使用私钥PrivateKey加密，公钥PublicKey 解密
        KeyPair keyPair = Keys.keyPairFor(algorithm);
        // 私钥
        return keyPair.getPrivate();
        //return Keys.hmacShaKeyFor(secreKeyBytes());
    }

    /**
     * 解析JWT使用的key
     * <p>使用HAMC 算法时和{@link #key4Issue(SignatureAlgorithm)}一致，
     * 使用密钥对加解密时，要用私钥加密，公钥解密。</p>
     * @return
     */
    private static Key key4Parse(SignatureAlgorithm algorithm) {
        try {
            return Keys.secretKeyFor(algorithm);
        } catch (IllegalArgumentException e) {
            log.info("$$$ 未采用 HAMC 算法。");
        }
        // 未采用 HMAC 算法时，使用私钥PrivateKey加密，公钥PublicKey 解密
        KeyPair keyPair = Keys.keyPairFor(algorithm);
        // 公钥
        return keyPair.getPublic();
    }

    private static byte[] secreKeyBytes() {
        // TODO
        return Base64.getEncoder().encode(DEFAULT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        //return DatatypeConverter.parseBase64Binary(DEFAULT_SECRET_KEY);
    }

    /**
     * 解析JWT的Payload（载荷——存放着有效信息）
     * <p>共有三部分，以.分隔，payload在第二部分</p>
     */
    public static String parseJwtPayload(String jwt) {
        Assert.hasText(jwt, "JWS 字符串不能为空！");
        String[] jwtParts = jwt.split(JWT_PARTS_SEPARATOR);
        if (ArrayUtils.getLength(jwtParts) != JWT_PARTS_COUNT) {
            throw new MalformedJwtException("JWS 字符串必须恰好包含2个 . 字符！");
        }

        // 头
        String base64UrlEncodedHeader = jwtParts[0];
        // 载荷
        String base64UrlEncodedPayload = jwtParts[1];
        // 签名
        String base64UrlEncodedSecret = jwtParts[2];

        if (StringUtils.isNotBlank(base64UrlEncodedHeader)) {
            throw new MalformedJwtException("JWS 字符串缺少header信息！");
        }

        if (StringUtils.isBlank(base64UrlEncodedPayload)) {
            throw new MalformedJwtException("JWS 字符串缺少payload信息！");
        }

        if (StringUtils.isBlank(base64UrlEncodedSecret)) {
            throw new MalformedJwtException("JWS 字符串缺少签名信息！");
        }

        /**
         * 解析 Header 中的压缩编解码器（payload可能使用了压缩算法）
         {
            "alg": "HS256",
            "typ": "JWT"
         }
         */
        String origValue = CryptoUtils.decodeBase64(base64UrlEncodedHeader);
        Map<String, Object> headerMap = JSON.parseObject(origValue, Map.class);
        Header header  = new DefaultJwsHeader(headerMap);
        // 压缩编解码器
        CompressionCodec compressionCodec = COMPRESSION_CODEC_RESOLVER.resolveCompressionCodec(header);

        // 获取 payload
        byte[] payloadBytes = Base64.getDecoder().decode(base64UrlEncodedPayload.getBytes(StandardCharsets.UTF_8));
        if (compressionCodec != null) {
            // 解压缩
            payloadBytes = compressionCodec.decompress(payloadBytes);
        }
        return new String(payloadBytes, StandardCharsets.UTF_8);
    }

    /**
     * 验签JWT
     *
     * @param jwt json web token
     */
    public static JwtAccount parseJwt(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return parseJwt(jwt, DEFAULT_ALGORITHM);
    }

    /**
     * 验签JWT
     *
     * @param jwt json web token
     */
    public static JwtAccount parseJwt(String jwt, SignatureAlgorithm algorithm) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = Jwts.parser()
                .setSigningKey(key4Parse(algorithm))
                //.setSigningKey(secreKeyBytes())
                .parseClaimsJws(jwt)
                .getBody();
        JwtAccount jwtAccount = new JwtAccount();
        //令牌ID
        jwtAccount.setTokenId(claims.getId());
        // 账户标识
        jwtAccount.setAccount(claims.getSubject());
        // 签发者
        jwtAccount.setIssuer(claims.getIssuer());
        // 签发时间
        jwtAccount.setIssuedAt(claims.getIssuedAt());
        // 接收方
        jwtAccount.setAudience(claims.getAudience());
        // 获取jwt中私有声明中的-角色
        jwtAccount.setRoles(claims.get(JwtConstants.CLAIMS_ROLES, String.class));
        // 获取jwt中私有声明中的-权限
        jwtAccount.setPerms(claims.get(JwtConstants.CLAIMS_PERMISSIONS, String.class));
        return jwtAccount;
    }
}
