package com.clsaa.dop.server.login.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clsaa.dop.server.login.config.BizCodes;
import com.clsaa.dop.server.login.config.CacheConfig;
import com.clsaa.dop.server.login.config.LoginProperties;
import com.clsaa.dop.server.login.enums.Client;
import com.clsaa.dop.server.login.model.bo.UserBoV1;
import com.clsaa.dop.server.login.model.po.LoginLog;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆业务实现类
 *
 * @author joyren
 */
@Service
public class LoginService {
    public static final String JWT_TOKEN_CACHE_PREFIX = CacheConfig.CacheNames.CACHE_EXPIRED_30_MINS + ":jwt:";

    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 登录
     *
     * @param email    电子邮箱
     * @param password 密码
     * @param loginIp  登录IP
     * @param deviceId 登录设备IP
     * @param client   登录客户端
     * @return {@link String} JWT token
     */
    public String login(String email, String password, String loginIp, String deviceId, Client client) {
        //查询用户信息
        UserBoV1 userBoV1 = this.userService.findUserByEmailAndPassword(email, password);
        if (userBoV1 == null) {
            //存储登录日志
            this.loginLogService.addLoginLog(userBoV1.getId(), loginIp, deviceId, client, LoginLog.Status.FAIL);
            BizAssert.justDenied(BizCodes.LOGIN_FAIL);
        }
        System.out.println(userBoV1.toString());

        //准备 JWT 签名算法
        Algorithm alg = null;
        try {
            alg = Algorithm.HMAC256(this.loginProperties.getJwt().getSecret());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //准备JWT所需数据
        //签发时间
        Date iatDate = new Date();
        //过期时间
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, this.loginProperties.getJwt().getExp());
        Date expiresDate = nowTime.getTime();
        //Header
        Map<String, Object> jwtHeaders = new HashMap<>();
        jwtHeaders.put(LoginProperties.Jwt.ALG_KEY, this.loginProperties.getJwt().getAlg());
        jwtHeaders.put(LoginProperties.Jwt.TYP_KEY, this.loginProperties.getJwt().getTyp());

        //构造JWT
        String token = JWT.create()
                .withHeader(jwtHeaders)
                .withIssuer(this.loginProperties.getJwt().getIss())
                .withAudience(this.loginProperties.getJwt().getAud())
                .withSubject(this.loginProperties.getJwt().getSub())
                .withIssuedAt(iatDate)
                .withExpiresAt(expiresDate)
                .withClaim("userId", userBoV1.getId())
                .withClaim("name", userBoV1.getName())
                .withClaim("email", userBoV1.getEmail())
                .withClaim("avatarUrl", StringUtils.isBlank(userBoV1.getAvatarURL()) ? "" : userBoV1.getAvatarURL())
                .withClaim("status", userBoV1.getStatus().name())
                .withClaim("client", client.name())
                .sign(alg);

        //存储JWT
        this.redisTemplate.opsForValue().set(LoginService.JWT_TOKEN_CACHE_PREFIX + ":" + client.name() + ":" + userBoV1.getId(), token);

        //存储登录日志
        this.loginLogService.addLoginLog(userBoV1.getId(), loginIp, deviceId, client, LoginLog.Status.SUCCESS);

        return token;
    }

    /**
     * 查询用户登录Token
     *
     * @param userId 用户id
     * @param client 客户端
     * @return {@link String} JWT
     */
    public String findUserLoginToken(Long userId, Client client) {
        return this.redisTemplate.opsForValue().get(LoginService.JWT_TOKEN_CACHE_PREFIX + ":" + client.name() + ":" + +userId);
    }

    /**
     * 登出
     *
     * @param token 用户登录Token
     */
    public void logout(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.loginProperties.getJwt().getSecret())).build();
            DecodedJWT jwt = verifier.verify(token);
            Long userId = jwt.getClaim("userId").asLong();
            String client = jwt.getClaim("client").asString();
            //删除Token
            this.redisTemplate.delete(LoginService.JWT_TOKEN_CACHE_PREFIX + ":" + client + ":" + userId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验token
     *
     * @param token token
     * @return 校验通过返回true
     */
    public boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.loginProperties.getJwt().getSecret())).build();
            DecodedJWT jwt = verifier.verify(token);
            Long userId = jwt.getClaim("userId").asLong();
            String client = jwt.getClaim("client").asString();
            String existToken = this.redisTemplate.opsForValue().get(LoginService.JWT_TOKEN_CACHE_PREFIX + ":" + client + ":" + +userId);
            if (existToken == null) {
                return false;
            }
            return token.equals(existToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
