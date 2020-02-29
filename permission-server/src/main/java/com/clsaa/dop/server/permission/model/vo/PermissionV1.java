package com.clsaa.dop.server.permission.model.vo;

import lombok.*;

import java.time.LocalDateTime;
/**
 * 功能点显示层对象
 *
 * @author lzy
 *
 *
 * @since :2019.3.7
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionV1 {

    /**
     * 功能点ID
     */
    private Long id;
    /**
     * 父功能点ID
     */
    private Long parentId;
    /**
     * 功能点名称
     */
    private String name;
    /**
     * 功能点描述
     */
    private String description;

    /**
     * 创建者名称
     */
    private String userName;

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
