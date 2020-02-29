package com.clsaa.dop.server.application.model.bo;

import com.clsaa.dop.server.application.model.po.Project;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 项目业务层对象
 * </p>
 *
 * @author Bowen
 * @since 2019-3-5
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBoV1 implements Serializable {

    private static final long serialVersionUID = 6906097418517275448L;
    /**
     * 项目id
     */
    private Long id;

    /**
     * 项目名称
     */
    private String title;

    /**
     * 创建人
     */
    private Long cuser;


    /**
     * 修改人
     */
    private Long muser;

    /**
     * 创建日期
     */
    private LocalDateTime ctime;

    /**
     * 修改日期
     */
    private LocalDateTime mtime;

    /**
     * 组织ID
     */
    private Long organizationId;

    /**
     * 是否删除
     */
    private boolean deleted;

    /**
     * 项目状态
     */
    private Project.Status status;

    /**
     * 项目描述
     */
    private String description;

}
