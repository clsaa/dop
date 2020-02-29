package com.clsaa.dop.server.login.service;

import com.clsaa.dop.server.login.dao.LoginLogRepository;
import com.clsaa.dop.server.login.enums.Client;
import com.clsaa.dop.server.login.model.po.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 登陆日志业务实现类
 *
 * @author joyren
 */
@Service
public class LoginLogService {
    @Autowired
    private LoginLogRepository loginLogRepository;

    /**
     * 添加登录日志
     *
     * @param userId   用户id
     * @param loginIp  登录ip
     * @param deviceId 登录设备
     * @param client   登录客户端
     * @param status   登录状态
     */
    public void addLoginLog(Long userId, String loginIp, String deviceId, Client client, LoginLog.Status status) {
        LoginLog loginLog = LoginLog.builder()
                .userId(userId)
                .loginIp(loginIp)
                .deviceId(deviceId)
                .client(client)
                .status(status)
                .ctime(LocalDateTime.now())
                .build();
        this.loginLogRepository.saveAndFlush(loginLog);
    }
}
