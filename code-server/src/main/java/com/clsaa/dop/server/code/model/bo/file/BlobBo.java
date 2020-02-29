package com.clsaa.dop.server.code.model.bo.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlobBo {

    private String file_name;//对应
    private int size;//对应json数据的字段
    private String file_size;//不对应
    private String file_content;//不对应
}
