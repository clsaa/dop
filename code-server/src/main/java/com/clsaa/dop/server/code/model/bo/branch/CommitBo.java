package com.clsaa.dop.server.code.model.bo.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitBo {

    private String message;
    private String authored_date;
    private String id;

}
