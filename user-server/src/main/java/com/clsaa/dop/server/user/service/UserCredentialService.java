package com.clsaa.dop.server.user.service;

import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.dao.UserCredentialRepository;
import com.clsaa.dop.server.user.model.bo.UserCredentialBoV1;
import com.clsaa.dop.server.user.model.po.UserCredential;
import com.clsaa.dop.server.user.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 用户凭据业务实现
 *
 * @author joyren
 */
@Service
public class UserCredentialService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    /**
     * 添加用户凭据
     *
     * @param userId     用户id
     * @param identifier 凭据标识
     * @param credential 凭据
     * @param type       凭据类型
     */
    public void addUserCredential(Long userId, String identifier, String credential, UserCredential.Type type) {
        UserCredential userCredential = UserCredential.builder()
                .userId(userId)
                .identifier(identifier)
                .credential(credential)
                .type(type)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .build();
        this.userCredentialRepository.saveAndFlush(userCredential);
    }

    /**
     * 根据类型和用户id更新用户凭据
     *
     * @param userId     用户id
     * @param credential 凭据
     */
    public void updateUserCredentialByUserIdAndType(Long userId, String credential, UserCredential.Type type) {
        UserCredential userCredential = this.userCredentialRepository
                .findUserCredentialByUserIdAndType(userId, type);
        BizAssert.found(userCredential != null, new BizCode(BizCodes.INVALID_PARAM.getCode(), "凭证标识不存在"));
        userCredential.setCredential(credential);
        userCredential.setMtime(LocalDateTime.now());
        this.userCredentialRepository.saveAndFlush(userCredential);
    }

    /**
     * 根据用户id和凭据类型查询用户凭据
     *
     * @param userId 用户id
     * @param type   凭据类型
     * @return {@link UserCredentialBoV1}
     */
    public UserCredentialBoV1 findUserCredentialByUserIdAndType(Long userId, UserCredential.Type type) {
        UserCredential userCredential = this.userCredentialRepository
                .findUserCredentialByUserIdAndType(userId, type);
        return BeanUtils.convertType(userCredential, UserCredentialBoV1.class);
    }

    /**
     * 根据用户id和凭据类型删除用户凭据
     *
     * @param userId 用户id
     * @param type   凭据类型
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void deleteUserCredentialByUserIdAndType(Long userId, UserCredential.Type type) {
        BizAssert.validParam(type != UserCredential.Type.DOP_LOGIN_EMAIL, BizCodes.INVALID_OPERATION_FOR_CREDENTIAL);
        this.userCredentialRepository.deleteUserCredentialByUserIdAndType(userId, type);
    }
}
