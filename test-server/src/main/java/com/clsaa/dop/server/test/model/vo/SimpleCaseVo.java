package com.clsaa.dop.server.test.model.vo;

import com.clsaa.dop.server.test.enums.CaseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xihao
 * @version 1.0
 * @since 07/05/2019
 */
@Data
public class SimpleCaseVo {

    private CaseType caseType;

    private Long id;

    private String caseName;

    private String caseDesc;

    private Long applicationId;

    private String searchInfo;

    public String getSearchInfo() {
        return caseType.toString() + "---【" + id + "】---" + caseName;
    }

    public SimpleCaseVo(Long id, String caseName, String caseDesc, Long applicationId) {
        this.id = id;
        this.caseName = caseName;
        this.caseDesc = caseDesc;
        this.applicationId = applicationId;
    }
}
