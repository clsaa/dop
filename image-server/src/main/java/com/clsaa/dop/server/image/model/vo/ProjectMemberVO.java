package com.clsaa.dop.server.image.model.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * 项目成员VO类
 * @author xzt
 * @since 2019-4-18
 */
@Getter
@Setter
public class ProjectMemberVO {
    private Integer id;

    private Integer projectId;

    private String entityName;

    private String roleName;

    private Integer roleId;

    private Integer entityId;

    private String entityType;
}
