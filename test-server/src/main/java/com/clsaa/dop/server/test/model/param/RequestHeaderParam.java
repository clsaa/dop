package com.clsaa.dop.server.test.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
public class RequestHeaderParam {

    @NotNull
    private String name;

    @NotNull
    private String value;

}
