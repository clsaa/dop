package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.model.po.CaseStatus;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xihao
 * @version 1.0
 * @since 16/03/2019
 */
@Data
public class InterfaceCaseParam {

    @NotBlank(message = "用例名不能为空")
    private String caseName;

    @NotBlank(message = "用例描述不能为空")
    private String caseDesc;

    @NotBlank(message = "前置条件不能为空")
    private String preCondition;

    @NotNull(message = "应用id不能为空")
    @Min(1)
    private Long applicationId;

    @NotBlank(message = "步骤不能为空")
    private String steps;

    @NotBlank(message = "预测结果不能为空")
    private String predicateResult;

    @NotNull(message = "用例状态不能为空")
    private CaseStatus status;

}
