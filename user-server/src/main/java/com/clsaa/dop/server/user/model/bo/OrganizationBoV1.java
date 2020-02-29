package com.clsaa.dop.server.user.model.bo;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织服务层对象，用户组织是组织架构中的顶层结构，组织包含多个组{@link Group}
 *
 * @author joyren
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationBoV1 implements Serializable {
    private static final long serialVersionUID = 6906097411517275871L;
    /**
     * 组织id
     */
    private Long id;
    /**
     * 组织名
     */
    private String name;
    /**
     * 组织描述
     */
    private String description;
    /**
     * 创建人
     */
    private Long cuser;
    /**
     * 修改人
     */
    private Long muser;
    /**
     * 所属人(组织拥有者)
     */
    private Long ouser;
    /**
     * 创建时间
     */
    private LocalDateTime ctime;
    /**
     * 修改时间
     */
    private LocalDateTime mtime;
    /**
     * 是否删除
     */
    private Boolean deleted;
}
