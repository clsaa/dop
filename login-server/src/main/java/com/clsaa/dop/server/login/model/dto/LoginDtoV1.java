package com.clsaa.dop.server.login.model.dto;

import com.clsaa.dop.server.login.enums.Client;
import lombok.Data;

/**
 * 登录，数据传输层对象
 *
 * @author joyren
 */
@Data
public class LoginDtoV1 {
    private String email;
    private String password;
    private String loginIp;
    private String deviceId;
    private Client client;
}
