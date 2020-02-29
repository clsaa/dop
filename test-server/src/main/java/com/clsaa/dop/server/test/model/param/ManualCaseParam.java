package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.model.po.CaseStatus;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 19/03/2019
 */
@Data
public class ManualCaseParam {

    @NotNull
    private String caseName;

    @NotNull
    private String caseDesc;

    @NotNull
    private String preCondition;

    @Min(1)
    private Long applicationId;

    @NotNull
    private String comment;

    @NotNull
    private CaseStatus status;

    @NotNull
    private List<TestStepParam> testSteps;
}
