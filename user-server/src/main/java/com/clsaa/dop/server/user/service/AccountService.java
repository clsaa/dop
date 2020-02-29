package com.clsaa.dop.server.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.config.CacheConfig;
import com.clsaa.dop.server.user.config.UserProperties;
import com.clsaa.dop.server.user.model.bo.UserBoV1;
import com.clsaa.dop.server.user.model.dto.EmailDtoV1;
import com.clsaa.dop.server.user.mq.MessageQueueException;
import com.clsaa.dop.server.user.mq.MessageSender;
import com.clsaa.dop.server.user.util.Validator;
import com.clsaa.dop.server.user.util.crypt.CryptoResult;
import com.clsaa.dop.server.user.util.crypt.RSA;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import com.google.common.io.BaseEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 账号服务类
 *
 * @author joyren
 */
@Service
public class AccountService {
    static final String REGISTER_CODE_KEY_PREFIX = CacheConfig.CacheNames.CACHE_EXPIRED_30_MINS + ".account.register.code.";
    static final String REGISTER_EMAIL_KEY_PREFIX = CacheConfig.CacheNames.CACHE_EXPIRED_30_MINS + ".account.register.email.";
    static final String RESET_BY_EMAIL_KEY_PREFIX = CacheConfig.CacheNames.CACHE_EXPIRED_30_MINS + ".account.reset.email.";

    @Autowired
    private UserService userService;
    @Autowired
    private UserProperties userProperties;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private MessageSender messageSender;
    @NotBlank
    @Value("${message.mq.RocketMQ.emailTopic}")
    private String emailTopic;
    @NotBlank
    @Value("${message.email.from}")
    private String emailFrom;
    @NotBlank
    @Value("${message.email.registerContent}")
    private String registerContent;
    @NotBlank
    @Value("${message.email.resetContent}")
    private String resetContent;


    public String getAccountRSAPublicKey() {
        return this.userProperties.getAccount().getSecret().getRSAPublicKey();
    }

    /**
     * 注册：缓存用户注册信息，发送用户激活邮件
     *
     * @param name     用户名
     * @param email    邮箱
     * @param password 密码
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void register(String name, String email, String password) {
        BizAssert.validParam(Validator.isUsername(name),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户名非法"));
        BizAssert.validParam(Validator.isEmail(email),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "邮箱格式错误"));
        CryptoResult realPassword = RSA.decryptByPrivateKey(password, this.userProperties.getAccount().getSecret().getRSAPrivateKey());
        BizAssert.validParam(realPassword.isOK(), new BizCode(BizCodes.INVALID_PARAM.getCode(), "RSA解密失败"));
        BizAssert.validParam(Validator.isPassword(realPassword.getContent()),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "密码格式错误"));
        //校验用户名或邮箱是否存在
        UserBoV1 userBoV1 = this.userService.findUserByEmail(email);
        BizAssert.allowed(userBoV1 == null, BizCodes.REPETITIVE_USER_EMAIL);
        userBoV1 = this.userService.findUserByName(name);
        BizAssert.allowed(userBoV1 == null, BizCodes.REPETITIVE_USER_NAME);
        //清除缓存中已有的注册信息
        String exitRegisterDataStr = this.redisTemplate.opsForValue().get(REGISTER_EMAIL_KEY_PREFIX + email);
        if (exitRegisterDataStr != null) {
            JSONObject registerData = JSONObject.parseObject(exitRegisterDataStr);
            String code = registerData.getString("code");
            this.redisTemplate.delete(REGISTER_CODE_KEY_PREFIX + code);
            this.redisTemplate.delete(REGISTER_EMAIL_KEY_PREFIX + email);
        }
        //准备code
        byte[] byteCode = new byte[128];
        SecureRandom random = new SecureRandom();
        random.nextBytes(byteCode);
        String code = BaseEncoding.base64Url().encode(byteCode);
        //发送邮件
        try {
            EmailDtoV1 emailDtoV1 = new EmailDtoV1(emailFrom, email, "您正在注册DOP系统", this.registerContent.replace(":code", code));
            this.messageSender.send(this.emailTopic, UUID.randomUUID().toString(), JSON.toJSONString(emailDtoV1));
        } catch (MessageQueueException e) {
            e.printStackTrace();
            return;
        }
        //重新添加注册信息，缓存在30分钟后过期
        JSONObject registerData = new JSONObject();
        registerData.put("name", name);
        registerData.put("email", email);
        registerData.put("password", password);
        registerData.put("code", code);
        this.redisTemplate.opsForValue().set(REGISTER_CODE_KEY_PREFIX + code, email, 30, TimeUnit.MINUTES);
        this.redisTemplate.opsForValue().set(REGISTER_EMAIL_KEY_PREFIX + email, registerData.toJSONString(), 30, TimeUnit.MINUTES);
    }

    /**
     * 修改用户密码
     *
     * @param email 邮件
     */
    public void resetAccountPassword(String email) {
        BizAssert.validParam(Validator.isEmail(email),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "邮箱格式错误"));
        UserBoV1 userBoV1 = this.userService.findUserByEmail(email);
        BizAssert.validParam(userBoV1 != null,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "未注册邮箱"));
        //准备code
        String code = UUID.randomUUID().toString().split("-")[0].toUpperCase();
        this.redisTemplate.opsForValue().set(RESET_BY_EMAIL_KEY_PREFIX + email, code, 5, TimeUnit.MINUTES);
        //发送邮件
        try {
            EmailDtoV1 emailDtoV1 = new EmailDtoV1(emailFrom, email, "您正在修改DOP系统密码", this.resetContent.replace(":code", code));
            this.messageSender.send(this.emailTopic, UUID.randomUUID().toString(), JSON.toJSONString(emailDtoV1));
        } catch (MessageQueueException e) {
            e.printStackTrace();
        }
    }
}
