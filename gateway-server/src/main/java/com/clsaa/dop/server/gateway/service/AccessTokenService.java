package com.clsaa.dop.server.gateway.service;

import com.clsaa.dop.server.gateway.dao.AccessTokenDao;
import com.clsaa.dop.server.gateway.model.bo.AccessTokenBoV1;
import com.clsaa.dop.server.gateway.model.po.AccessToken;
import com.clsaa.dop.server.gateway.util.BeanUtils;
import com.clsaa.dop.server.gateway.util.TimestampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * AccessToken业务逻辑
 *
 * @author 任贵杰
 */
@Service
public class AccessTokenService {
    @Autowired
    private AccessTokenDao accessTokenDao;

    /**
     * 创建access token
     *
     * @param clientId            客户端主键id
     * @param accessTokenValidity access token存活时长
     * @return {@link AccessTokenBoV1}
     */
    public AccessTokenBoV1 addAccessToken(Long clientId, Integer accessTokenValidity) {
        AccessToken accessToken = AccessToken.builder()
                .clientId(clientId)
                .token(UUID.randomUUID().toString())
                .expires(new Timestamp(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(accessTokenValidity)))
                .ctime(TimestampUtil.now())
                .build();
        return BeanUtils.convertType(this.accessTokenDao.saveAndFlush(accessToken), AccessTokenBoV1.class);
    }


    /**
     * 根据token查询access token
     *
     * @param token 已颁发的token
     * @return {@link AccessTokenBoV1}
     */
    public AccessTokenBoV1 findAccessTokenByToken(String token) {
        return BeanUtils.convertType(this.accessTokenDao.findAccessTokenByToken(token), AccessTokenBoV1.class);
    }
}
