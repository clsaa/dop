package com.clsaa.dop.server.test.model.param.update;

import com.clsaa.dop.server.test.model.dto.CaseParamDto;
import com.clsaa.dop.server.test.model.param.UpdateParam;
import com.clsaa.dop.server.test.model.po.CaseStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 16/04/2019
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatedInterfaceCase implements UpdateParam<Long> {

    private Long id;

    private String caseName;

    private String caseDesc;

    private String preCondition;

    private Long applicationId;

    private String steps;

    private String predicateResult;

    private CaseStatus status;

    private List<CaseParamDto> caseParams;

}
