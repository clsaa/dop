package com.clsaa.dop.server.application.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 变量创建视图层层对象
 *
 * @author Bowen
 * @since 2019-3-12
 **/
@Getter
@Setter
public class AppVarCreateV1 {
    /**
     * 键
     */
    private String varKey;
    /**
     * 值
     */
    private String varValue;
}
