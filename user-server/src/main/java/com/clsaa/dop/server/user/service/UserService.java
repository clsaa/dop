package com.clsaa.dop.server.user.service;

import com.alibaba.fastjson.JSONObject;
import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.config.UserProperties;
import com.clsaa.dop.server.user.dao.UserRepository;
import com.clsaa.dop.server.user.model.bo.UserBoV1;
import com.clsaa.dop.server.user.model.bo.UserCredentialBoV1;
import com.clsaa.dop.server.user.model.po.User;
import com.clsaa.dop.server.user.model.po.UserCredential;
import com.clsaa.dop.server.user.util.BeanUtils;
import com.clsaa.dop.server.user.util.Validator;
import com.clsaa.dop.server.user.util.crypt.CryptoResult;
import com.clsaa.dop.server.user.util.crypt.RSA;
import com.clsaa.dop.server.user.util.crypt.StrongPassword;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户业务实现类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProperties userProperties;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserCredentialService userCredentialService;

    /**
     * <p>
     * 添加新用户，如果邮箱已经被注册则会抛出异常
     * </p>
     *
     * @author 任贵杰 812022339@qq.com
     * @since 2018-12-23
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void addUser(String code) {
        //获取缓存中已有的注册信息
        String email = this.redisTemplate.opsForValue().get(AccountService.REGISTER_CODE_KEY_PREFIX + code);
        BizAssert.validParam(email != null, BizCodes.EXPIRED_REGISTER_CODE);
        String exitRegisterDataStr = this.redisTemplate.opsForValue().get(AccountService.REGISTER_EMAIL_KEY_PREFIX + email);
        if (exitRegisterDataStr == null) {
            this.redisTemplate.delete(AccountService.REGISTER_CODE_KEY_PREFIX + code);
        }
        BizAssert.validParam(exitRegisterDataStr != null, BizCodes.EXPIRED_REGISTER_CODE);
        if (exitRegisterDataStr != null) {
            JSONObject registerData = JSONObject.parseObject(exitRegisterDataStr);
            //RSA解密
            String password = registerData.getString("password");
            CryptoResult realPassword = RSA.decryptByPrivateKey(password, this.userProperties.getAccount().getSecret().getRSAPrivateKey());
            BizAssert.validParam(realPassword.isOK(), new BizCode(BizCodes.INVALID_PARAM.getCode(), "RSA解密失败"));
            //校验用户名或邮箱是否存在
            User existUser = this.userRepository.findUserByEmail(email);
            BizAssert.allowed(existUser == null, BizCodes.REPETITIVE_USER_EMAIL);
            existUser = this.userRepository.findUserByName(registerData.getString("name"));
            BizAssert.allowed(existUser == null, BizCodes.REPETITIVE_USER_NAME);
            User user = User.builder()
                    .name(registerData.getString("name"))
                    .email(registerData.getString("email"))
                    .ctime(LocalDateTime.now())
                    .mtime(LocalDateTime.now())
                    .status(User.Status.NORMAL)
                    .avatarURL("")
                    .build();
            User savedUser = this.userRepository.saveAndFlush(user);
            try {
                String strongPassword = StrongPassword.encrypt(realPassword.getContent()).getContent();
                this.userCredentialService.addUserCredential(savedUser.getId(),
                        email, strongPassword, UserCredential.Type.DOP_LOGIN_EMAIL);
            } catch (Exception e) {
                BizAssert.justInvalidParam(BizCodes.REPETITIVE_USER_EMAIL);
            }
            //注册成功清除缓存信息
            this.redisTemplate.delete(AccountService.REGISTER_CODE_KEY_PREFIX + code);
            this.redisTemplate.delete(AccountService.REGISTER_EMAIL_KEY_PREFIX + email);
        }
    }


    /**
     * 根据id查询用户，若不存在返回null
     */
    public UserBoV1 findUserById(Long id) {
        return BeanUtils.convertType(this.userRepository.findUsersById(id), UserBoV1.class);
    }

    /**
     * 根据邮箱查询用户，若不存在返回null
     */
    public UserBoV1 findUserByEmail(String email) {
        return BeanUtils.convertType(this.userRepository.findUserByEmail(email), UserBoV1.class);
    }

    /**
     * 根据用户名查询用户，若不存在返回null
     */
    public UserBoV1 findUserByName(String name) {
        return BeanUtils.convertType(this.userRepository.findUserByName(name), UserBoV1.class);
    }

    /**
     * 修改用户密码
     *
     * @param email    电子邮箱
     * @param password 密码
     * @param code     验证码
     */
    public void updateUserPassword(String email, String password, String code) {
        String cachedCode = this.redisTemplate.opsForValue().get(AccountService.RESET_BY_EMAIL_KEY_PREFIX + email);
        BizAssert.validParam(cachedCode.equals(code),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "验证码错误"));
        CryptoResult realPassword = RSA.decryptByPrivateKey(password, this.userProperties.getAccount().getSecret().getRSAPrivateKey());
        BizAssert.validParam(realPassword.isOK(), new BizCode(BizCodes.INVALID_PARAM.getCode(), "RSA解密失败"));
        BizAssert.validParam(Validator.isPassword(realPassword.getContent()),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "密码格式错误"));
        User user = this.userRepository.findUserByEmail(email);
        BizAssert.validParam(user != null,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "未注册邮箱"));
        String strongPassword = StrongPassword.encrypt(realPassword.getContent()).getContent();
        System.out.println("strong password : " + strongPassword);
        this.userCredentialService.updateUserCredentialByUserIdAndType(user.getId(), strongPassword, UserCredential.Type.DOP_LOGIN_EMAIL);
        this.redisTemplate.delete(AccountService.RESET_BY_EMAIL_KEY_PREFIX + email);
    }

    public UserBoV1 findUserByEmailAndPassword(String email, String password) {
        User user = this.userRepository.findUserByEmail(email);
        BizAssert.validParam(user != null,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "未注册邮箱"));
        UserCredentialBoV1 userCredentialBoV1 = this.userCredentialService
                .findUserCredentialByUserIdAndType(user.getId(), UserCredential.Type.DOP_LOGIN_EMAIL);
        BizAssert.validParam(userCredentialBoV1 != null,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "未找到对应用户凭据"));
        BizAssert.validParam(email.equals(userCredentialBoV1.getIdentifier()),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户凭据中的邮箱与用户邮箱不一致"));
        CryptoResult realPassword = RSA.decryptByPrivateKey(password, this.userProperties.getAccount().getSecret().getRSAPrivateKey());
        BizAssert.validParam(realPassword.isOK(), new BizCode(BizCodes.INVALID_PARAM.getCode(), "RSA解密失败"));
        System.out.println(realPassword.getContent());
        System.out.println(userCredentialBoV1.getCredential());
        boolean verifyResult = StrongPassword.verify(realPassword.getContent(), userCredentialBoV1.getCredential());
        System.out.println(verifyResult);
        BizAssert.pass(verifyResult, BizCodes.INVALID_PASSWORD);
        return BeanUtils.convertType(user, UserBoV1.class);
    }
}
