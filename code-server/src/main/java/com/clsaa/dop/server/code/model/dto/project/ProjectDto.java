package com.clsaa.dop.server.code.model.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long userId;
    private String name;
    private String description;
    private String visibility;
    private String initialize_with_readme;
}
