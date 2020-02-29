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
public class ChildrenVo {

    private String value;
    private String label;
    private boolean default_;
    private boolean protected_;

}
