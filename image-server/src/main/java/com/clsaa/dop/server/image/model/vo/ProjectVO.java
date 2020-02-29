package com.clsaa.dop.server.image.model.vo;

import com.clsaa.dop.server.image.model.po.ProjectMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 *     项目视图层对象
 * </p>
 * @author xzt
 * @since  2019-3-25
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVO {
    /**
     * 项目的id
     */
    private Integer projectId;
    /**
     * 项目创建者的id
     */
    private Integer ownerId;
    /**
     * 项目的名称
     */
    private String name;
    /**
     * 项目创建时间
     */
    private String creationTime;
    /**
     * 项目最后修改时间
     */
    private String updateTime;
    /**
     * 项目的状态，是否被删除
     */
    private Boolean deleted;
    /**
     * 项目创建者的名字
     */
    private String ownerName;
    /**
     * 用来控制项目的public属性是否可以被修改
     */
    private Boolean togglable;
    /**
     *  当前用户的用户类型
     */
    private String currentUserRole;
    /**
     *  镜像仓库数
     */
    private Integer repoCount;
    /**
     * chart数量
     */
    private Integer chartCount;
    /**
     * 项目基本设置信息
     */
    private ProjectMetadata metadata;
}
