package com.clsaa.dop.server.permission.model.bo;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户规则表BO层
 *
 * @author lzy
 *
 * since :2019.3.21
 */


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRuleBoV1 implements Serializable {
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
    private boolean deleted;
    /* 表里都要有的字段*/

}
