package com.clsaa.dop.server.code.model.bo.branch;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchBo {

    private String name;

    @JSONField(name="default")
    private String default_;

    @JSONField(name="protected")
    private String protected_;

    private String merged;

    private boolean developers_can_push;
    private boolean developers_can_merge;

    private String commit_id;
    private String commit_short_id;
    private String commit_msg;
    private String commit_time;
    private CommitBo commit;


}
