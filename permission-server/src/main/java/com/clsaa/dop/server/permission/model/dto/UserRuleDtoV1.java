package com.clsaa.dop.server.permission.model.dto;


/**
 * 用户规则表DTO层
 *
 * @author lzy
 *
 * since :2019.4.6
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRuleDtoV1 implements Serializable {
    /**
     * 用户数据规则ID
     */
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 权限作用域参数名
     */
    private String fieldName;
    /**
     * 规则
     */
    private String rule;
    /**
     * 描述
     */
    private String description;
    /* 表里都要有的字段*/
    /**
     * 创建时间
     */
    private LocalDateTime ctime;
    /**
     * 创建者
     */
    private Long cuser;

    /* 表里都要有的字段*/

}