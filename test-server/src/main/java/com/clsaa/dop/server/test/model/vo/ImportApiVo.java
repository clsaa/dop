package com.clsaa.dop.server.test.model.vo;

import com.clsaa.dop.server.test.model.param.CaseParamRef;
import com.clsaa.dop.server.test.model.param.RequestScriptParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportApiVo {

    private List<RequestScriptParam> requestScripts;

    private List<CaseParamRef> caseParams;
}
