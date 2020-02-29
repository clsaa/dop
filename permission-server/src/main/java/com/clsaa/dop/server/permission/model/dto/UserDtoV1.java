package com.clsaa.dop.server.permission.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户DTO
 * </p>
 *
 * @author lzy
 * @since 2019.4.7
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDtoV1  {
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
     * 创建时间
     */
    private LocalDateTime ctime;
    /**
     * 修改时间
     */
    private LocalDateTime mtime;

    public UserDtoV1(){
        super();
    }

}