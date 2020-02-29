package com.clsaa.dop.server.code.model.bo.file;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchBo {

    private String name;

    @JSONField(name="default")
    private boolean default_;

    @JSONField(name="protected")
    private boolean protected_;


}
