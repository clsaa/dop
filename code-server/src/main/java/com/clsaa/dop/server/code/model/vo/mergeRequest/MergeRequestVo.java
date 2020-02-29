package com.clsaa.dop.server.code.model.vo.mergeRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergeRequestVo {

    private int iid;
    private String title;
    private String description;
    private String state;
    private String created_by;
    private String created_at;
    private String updated_at;
    private String target_branch;
    private String source_branch;
    private String merged_by;
    private String merged_at;
    private String closed_by;
    private String closed_at;
    private String merge_status;
    private String changes_count;

}
