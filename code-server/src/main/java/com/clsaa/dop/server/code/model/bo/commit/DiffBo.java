package com.clsaa.dop.server.code.model.bo.commit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiffBo {
    private String diff;
    private String new_path;
    private String old_path;
    private String a_mode;
    private String b_mode;
    private boolean new_file;
    private boolean renamed_file;
    private boolean deleted_file;
}
