package com.clsaa.dop.server.user.dao;

import com.clsaa.dop.server.user.model.po.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户凭据Repository
 *
 * @author joyren
 */
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    /**
     * 通过用户id和凭据类型查询凭据
     *
     * @param userId 用户id
     * @param type   凭据类型
     * @return {@link UserCredential}
     */
    UserCredential findUserCredentialByUserIdAndType(Long userId, UserCredential.Type type);


    /**
     * 通过用户id和凭证类型删除凭证
     *
     * @param userId 用户id
     * @param type   凭证类型
     */
    void deleteUserCredentialByUserIdAndType(Long userId, UserCredential.Type type);
}
