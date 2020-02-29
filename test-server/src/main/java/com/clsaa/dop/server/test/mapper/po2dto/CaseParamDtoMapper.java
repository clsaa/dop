package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.CaseParamDto;
import com.clsaa.dop.server.test.model.po.CaseParam;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 11/04/2019
 */
@Component
public class CaseParamDtoMapper extends AbstractCommonServiceMapper<CaseParam, CaseParamDto> {

    @Override
    public Class<CaseParam> getSourceClass() {
        return CaseParam.class;
    }

    @Override
    public Class<CaseParamDto> getTargetClass() {
        return CaseParamDto.class;
    }

    @Override
    public Optional<CaseParam> inverseConvert(CaseParamDto caseParamDto) {
        return super.inverseConvert(caseParamDto)
                .map(UserManager.newInfoIfNotExists());
    }
}
