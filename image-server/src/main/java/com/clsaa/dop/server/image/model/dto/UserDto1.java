package com.clsaa.dop.server.image.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *     接收用户服务发送的消息的传输类
 * </p>
 * @author xzt
 * @since 2019-3-28
 */
@Getter
@Setter
public class UserDto1 {
    private String email;
    private Long id;
    private String password;
    private String name;
}
