package com.clsaa.dop.server.permission.model.bo;

import lombok.*;


import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 功能点业务层对象
 *
 * @author lzy
 *

 *
 * @since :2019.3.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionBoV1 implements Serializable {

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
     * 是否私有
     */
    private Integer isPrivate;
    /**
     * 功能点描述
     */
    private String description;

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
