package com.clsaa.dop.server.code.model.vo.file;

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
public class BranchAndTagVo {

    private String value;
    private String label;
    private List<ChildrenVo> children;
}
