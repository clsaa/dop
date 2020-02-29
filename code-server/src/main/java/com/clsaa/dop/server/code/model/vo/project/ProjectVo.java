package com.clsaa.dop.server.code.model.vo.project;

import lombok.Data;

/**
 * @author wsy
 */
@Data
public class ProjectVo {

    private int id;
    private String description;
    private String default_branch;
    private String name;
    private String ssh_url_to_repo;
    private String http_url_to_repo;
    private int star_count;
    private int forks_count;
    private String visibility;
    private int tag_count;
    private String file_size;
    private int commit_count;
    private int branch_count;


}
