package com.clsaa.dop.server.code.model.vo.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchVo {

    private String name;
    private String default_;
    private String protected_;
    private String merged;
    private boolean developers_can_push;
    private boolean developers_can_merge;
    private String commit_id;
    private String commit_short_id;
    private String commit_msg;
    private String commit_time;

}
