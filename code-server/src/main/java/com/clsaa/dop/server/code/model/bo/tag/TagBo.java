package com.clsaa.dop.server.code.model.bo.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagBo {
    private String name;
    private String message;


    private String commit_id;
    private String commit_short_id;
    private String commit_msg;
    private String commit_time;
    private CommitBo commit;
}
