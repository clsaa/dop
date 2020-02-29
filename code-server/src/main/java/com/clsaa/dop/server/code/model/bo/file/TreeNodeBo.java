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
public class TreeNodeBo {

    private String name;
    private String type;
    private String path;

}
