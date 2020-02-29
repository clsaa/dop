package com.clsaa.dop.server.test.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xihao
 * @version 1.0
 * @since 11/04/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseParamRef {

    @NotNull
    private String ref;

    @NotNull
    private String value;

    @NotNull
    @Min(1)
    private Long caseId;

}
