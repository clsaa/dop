package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.ExecuteWay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CaseGroupParam {

    private Long appId;

    private ExecuteWay executeWay;

    private String groupName;

    private String comment;

    private List<CaseUnitParam> caseUnits;
}
