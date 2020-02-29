package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.CaseType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaseUnitParam {

    private Long groupId;

    private CaseType caseType;

    private Long caseId;

    private String caseName;
}
