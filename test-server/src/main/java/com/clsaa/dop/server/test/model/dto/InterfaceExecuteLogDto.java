package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.CaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 31/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceExecuteLogDto {

    private CaseType caseType;

    private String jenkinsInfo;

    private String executeInfo;

    private Boolean success;

    private LocalDateTime begin;

    private LocalDateTime end;

    private Long caseId;

    private String caseName;

    private String createUserName;

    private List<OperationExecuteLogDto> operationExecuteLogs;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
