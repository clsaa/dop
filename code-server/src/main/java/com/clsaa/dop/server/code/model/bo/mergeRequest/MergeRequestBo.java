package com.clsaa.dop.server.code.model.bo.mergeRequest;

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
public class MergeRequestBo {
    private int iid;
    private String title;
    private String description;
    private String state;
    private UserBo author;
    private String created_by;
    private String created_at;
    private String updated_at;
    private String target_branch;
    private String source_branch;

    @JSONField(name="merged_by")
    private UserBo mergedBy;
    private String merged_by;
    private String merged_at;

    @JSONField(name="closed_by")
    private UserBo closedBy;
    private String closed_by;
    private String closed_at;

    private String merge_status;
    private String changes_count;
}
