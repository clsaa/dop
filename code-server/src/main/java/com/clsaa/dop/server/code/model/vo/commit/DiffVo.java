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
public class DiffVo {
    private String diff;
    private String new_path;
    private String old_path;
    private String a_mode;
    private String b_mode;
    private boolean new_file;
    private boolean renamed_file;
    private boolean deleted_file;
}
