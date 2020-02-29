package com.clsaa.dop.server.test.model.vo;

import lombok.Data;

/**
 * @author xihao
 * @version 1.0
 * @since 18/04/2019
 */
@Data
public class InterfaceCaseVo {

    private String caseName;

    private String caseDesc;

    private String preCondition;

    private Long applicationId;

    private String steps;

    private String predicateResult;

    private String createUserName;

}
