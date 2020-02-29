package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.CheckPointOperation;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
public class RequestCheckPointParam {

    @NotNull
    private String property;

    @NotNull
    private CheckPointOperation operation;

    @NotNull
    private String value;
}
