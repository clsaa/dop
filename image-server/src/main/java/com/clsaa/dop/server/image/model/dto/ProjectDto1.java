package com.clsaa.dop.server.image.model.dto;

import com.clsaa.dop.server.image.model.po.ProjectMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 项目传输层对象
 * @author xzt
 * @since 2019-3-24
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto1 {
    private String projectName;
    private ProjectMetadata metadata;
}
