package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.CaseUnitDto;
import com.clsaa.dop.server.test.model.po.CaseUnit;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@Component
public class CaseUnitDtoMapper extends AbstractCommonServiceMapper<CaseUnit, CaseUnitDto> {

    @Override
    public Class<CaseUnit> getSourceClass() {
        return CaseUnit.class;
    }

    @Override
    public Class<CaseUnitDto> getTargetClass() {
        return CaseUnitDto.class;
    }

    @Override
    public Optional<CaseUnit> inverseConvert(CaseUnitDto caseUnitDto) {
        return super.inverseConvert(caseUnitDto).map(UserManager.newInfoIfNotExists());
    }
}
