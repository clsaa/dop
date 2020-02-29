package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.CaseGroupDto;
import com.clsaa.dop.server.test.model.dto.CaseUnitDto;
import com.clsaa.dop.server.test.model.po.CaseGroup;
import com.clsaa.dop.server.test.model.po.CaseUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@Component
public class CaseGroupDtoMapper extends AbstractCommonServiceMapper<CaseGroup, CaseGroupDto> {

    @Autowired
    private CaseUnitDtoMapper caseUnitDtoMapper;

    @Override
    public Class<CaseGroup> getSourceClass() {
        return CaseGroup.class;
    }

    @Override
    public Class<CaseGroupDto> getTargetClass() {
        return CaseGroupDto.class;
    }

    @Override
    public Optional<CaseGroupDto> convert(CaseGroup caseGroup) {
        List<CaseUnitDto> unitDtos = caseUnitDtoMapper.convert(caseGroup.getCaseUnits());
        return super.convert(caseGroup).map(caseGroupDto -> {
            caseGroupDto.setCaseUnits(unitDtos);
            Long cuserId = caseGroupDto.getCuser();
            caseGroupDto.setCreateUserName(UserManager.getUserName(cuserId));
            return caseGroupDto;
        });
    }

    @Override
    public Optional<CaseGroup> inverseConvert(CaseGroupDto caseGroupDto) {
        List<CaseUnit> caseUnits = caseUnitDtoMapper.inverseConvert(caseGroupDto.getCaseUnits());
        return super.inverseConvert(caseGroupDto)
                .map(UserManager.newInfoIfNotExists())
                .map(caseGroup -> {
                    caseUnits.forEach(caseUnit -> caseUnit.setCaseGroup(caseGroup));
                    caseGroup.setCaseUnits(caseUnits);
                    return caseGroup;
                });
    }
}
