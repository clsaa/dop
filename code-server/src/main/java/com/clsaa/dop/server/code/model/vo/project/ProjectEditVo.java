package com.clsaa.dop.server.code.model.vo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEditVo {
    private int id;
    private String name;
    private String description;
    private String default_branch;
    private String visibility;
}
