package com.clsaa.dop.server.code.model.bo.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchAndTagBo {

    private String value;
    private String label;
    private List<ChildrenBo> children;
}
