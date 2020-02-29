package com.clsaa.dop.server.login.model.dto;

import com.clsaa.dop.server.login.model.bo.UserBoV1;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户视图层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDtoV1 {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户email地址
     */
    private String email;
    /**
     * 用户头像URL
     */
    private String avatarURL;
    /**
     * 用户状态
     */
    private UserBoV1.Status status;
}