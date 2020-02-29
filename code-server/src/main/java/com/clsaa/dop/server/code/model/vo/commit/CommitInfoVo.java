package com.clsaa.dop.server.code.model.vo.commit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitInfoVo {
    private String message;
    private String author_name;
    private String authored_date;
    private String authored_time;
    private String short_id;
    private String id;
    private int additions;
    private int deletions;
}
