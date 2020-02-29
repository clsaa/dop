package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.model.po.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManualCaseDto {

    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

    private String caseName;

    private String caseDesc;

    private String preCondition;

    private Long applicationId;

    private String comment;

    private CaseStatus status;
}
