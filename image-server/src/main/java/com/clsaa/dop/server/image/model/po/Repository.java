package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Repository {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("project_id")
    private Integer projectId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("pull_count")
    private Integer pullCount;

    @JsonProperty("star_count")
    private Integer starCount;

    @JsonProperty("tags_count")
    private Integer tagsCount;

    @JsonProperty("labels")
    private List<Label> labels;

    @JsonProperty("creation_time")
    private String creationTime;

    @JsonProperty("update_time")
    private String updateTime;
}
