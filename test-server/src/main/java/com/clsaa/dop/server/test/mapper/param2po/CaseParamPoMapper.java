package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.CaseParamRef;
import com.clsaa.dop.server.test.model.po.CaseParam;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;

/**
 * @author xihao
 * @version 1.0
 * @since 11/04/2019
 */
@Component
public class CaseParamPoMapper extends AbstractCommonServiceMapper<CaseParamRef, CaseParam> {

    @Override
    public Class<CaseParamRef> getSourceClass() {
        return CaseParamRef.class;
    }

    @Override
    public Class<CaseParam> getTargetClass() {
        return CaseParam.class;
    }

    @Override
    public Optional<CaseParam> convert(CaseParamRef caseParamRef) {
        return super.convert(caseParamRef)
                .map(dateAndUser())
                .map(caseParam -> {
                    InterfaceCase interfaceCase = new InterfaceCase();
                    interfaceCase.setId(caseParamRef.getCaseId());
                    interfaceCase.setCaseParams(Collections.singletonList(caseParam));
                    caseParam.setInterfaceCase(interfaceCase);
                    return caseParam;
                });
    }
}
