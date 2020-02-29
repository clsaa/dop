package com.clsaa.dop.server.user.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 注册数据传输层对象
 *
 * @author joyren
 */
@Getter
@Setter
public class RegisterDtoV1 {
    private String name;
    private String email;
    private String password;
}
