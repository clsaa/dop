package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目人员的harbor对应实体类
 * @author xzt
 * @since 2019-3-25
 */
@Getter
@Setter
public class ProjectMemberEntity   {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("project_id")
    private Integer projectId;

    @JsonProperty("entity_name")
    private String entityName;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("role_id")
    private Integer roleId;

    @JsonProperty("entity_id")
    private Integer entityId;

    @JsonProperty("entity_type")
    private String entityType;
}
