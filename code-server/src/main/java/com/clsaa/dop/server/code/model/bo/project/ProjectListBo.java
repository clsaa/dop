package com.clsaa.dop.server.code.model.bo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListBo {

    private int id;
    private String description;
    private String name;
    private String name_with_namespace;
    private String path_with_namespace;
    private int star_count;
    private String visibility;
}
