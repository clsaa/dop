package com.clsaa.dop.server.code.model.bo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEditBo {
    private int id;
    private String name;
    private String description;
    private String default_branch;
    private String visibility;
}
