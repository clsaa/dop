package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.ExecuteWay;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
public class CaseGroupDto {

    private Long appId;

    private ExecuteWay executeWay;

    private String groupName;

    private String comment;

    private List<CaseUnitDto> caseUnits;

    private String createUserName;

    private Integer caseCount;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

    public Integer getCaseCount() {
        return caseUnits.size();
    }
}
