package com.clsaa.dop.server.gateway.dao;

import com.clsaa.dop.server.gateway.model.po.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AccessToken持久层类
 *
 * @author 任贵杰 812022339@qq.com
 */
public interface AccessTokenDao extends JpaRepository<AccessToken, Long> {
    /**
     * 根据token查找AccessToken
     *
     * @param token 已颁发的token
     * @return {@link AccessToken}
     */
    AccessToken findAccessTokenByToken(String token);

}
