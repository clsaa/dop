package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *     对应的harbor的Label类
 * </p>
 * @author xzt
 * @since 2019-3-24
 */
@Setter
@Getter
public class Label {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("color")
    private String color;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("project_id")
    private Integer projectId;

    @JsonProperty("creation_time")
    private String creationTime;

    @JsonProperty("update_time")
    private String updateTime;

    @JsonProperty("deleted")
    private Boolean deleted;
}
