package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.ParamClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestParamCreateParam {

    private ParamClass paramClass;

    @NotNull
    private String name;

    @NotNull
    private String value;

}
