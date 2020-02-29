package com.clsaa.dop.server.code.model.bo.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitBo {

    private String id;
    private String committed_date;
    private String message;

}
