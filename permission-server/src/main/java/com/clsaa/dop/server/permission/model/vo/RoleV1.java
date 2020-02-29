package com.clsaa.dop.server.permission.model.vo;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 角色显示层对象
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
public class RoleV1 {

    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;

    /**
     * 创建者名称
     */
    private String userName;

    /* 表里都要有的字段*/

    /**
     * 修改时间
     */
    private LocalDateTime mtime;
    /**
     * 修改者
     */
    private Long muser;

    /* 表里都要有的字段*/

}
