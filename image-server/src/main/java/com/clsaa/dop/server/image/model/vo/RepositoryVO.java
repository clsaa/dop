package com.clsaa.dop.server.image.model.vo;

import com.clsaa.dop.server.image.model.po.Label;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RepositoryVO {
    private Integer id;
    private String name;
    private Integer projectId;
    private String description;
    private Integer pullCount;
    private Integer starCount;
    private Integer tagsCount;
    private List<Label> labels;
    private String creationTime;
    private String updateTime;
}
