package com.clsaa.dop.server.code.model.bo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBo {

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
    private StatisticsBo statistics;

}
