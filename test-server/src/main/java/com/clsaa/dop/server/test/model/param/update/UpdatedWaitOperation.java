package com.clsaa.dop.server.test.model.param.update;

import com.clsaa.dop.server.test.model.param.UpdateParam;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author xihao
 * @version 1.0
 * @since 19/04/2019
 */
@Data
public class UpdatedWaitOperation implements UpdateParam<Long> {

    private Long id;

    /**
     * 单位为毫秒
     */
    @Min(1)
    private int waitTime;

    private int order;
}
