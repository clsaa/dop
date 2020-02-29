package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.ParamType;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
public class RequestResultParam {

    @NotNull
    private ParamType paramType;

    @NotNull
    private String name;

    @NotNull
    private String rawValue;

    @NotNull
    private String paramDesc;
}
