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
public class ChildrenBo {

    private String value;
    private String label;
    @JSONField(name="default")
    private boolean default_;

    @JSONField(name="protected")
    private boolean protected_;

    public ChildrenBo(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
