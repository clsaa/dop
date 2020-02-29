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
public class ProjectUrlBo {
    private String http_url_to_repo;
    private String visibility;
    private PermissonsBo permissions;
}
