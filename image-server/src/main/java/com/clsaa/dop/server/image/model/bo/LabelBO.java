package com.clsaa.dop.server.image.model.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * label的业务层实体类
 * @author xzt
 * @since 2019-4-6
 */
@Getter
@Setter
public class LabelBO {
    private Integer id;

    private String name;

    private String description;

    private String color;

    private String scope;

    private Integer projectId;

    private String creationTime;

    private String updateTime;

    private Boolean deleted;
}
