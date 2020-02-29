package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.OperationType;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
public class WaitOperationParam {

    @NotNull
    private OperationType operationType;

    /**
     * 单位为毫秒
     */
    @Min(1)
    private int waitTime;

    private int order;
}
