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
public class TreeNodeVo {

    private String name;
    private String type;
    private String path;
}
