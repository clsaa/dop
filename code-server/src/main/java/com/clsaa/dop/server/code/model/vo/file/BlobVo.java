package com.clsaa.dop.server.code.model.vo.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlobVo {

    private String file_name;
    private int size;
    private String file_size;
    private String file_content;
}
