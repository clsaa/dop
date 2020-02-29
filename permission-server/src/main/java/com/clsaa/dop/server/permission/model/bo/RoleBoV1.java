package com.clsaa.dop.server.permission.model.bo;

import lombok.*;


import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 角色业务层对象
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
public class RoleBoV1 implements Serializable {

    /**
     * 角色ID
     */
    private Long id;
    /**
     * 父级角色ID
     */
    private Long parentId;
    /**
     * 角色名称
     */
    private String name;

    /* 表里都要有的字段*/
    /**
     * 创建时间
     */
    private LocalDateTime ctime;
    /**
     * 修改时间
     */
    private LocalDateTime mtime;
    /**
     * 创建者
     */
    private Long cuser;
    /**
     * 修改者
     */
    private Long muser;
    /**
     * 删除标记
     */
    private Boolean deleted;
    /* 表里都要有的字段*/
}
