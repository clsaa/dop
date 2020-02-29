package com.clsaa.dop.server.test.service.update;

import com.clsaa.dop.server.test.dao.CaseGroupRepository;
import com.clsaa.dop.server.test.mapper.po2dto.CaseGroupDtoMapper;
import com.clsaa.dop.server.test.model.dto.CaseGroupDto;
import com.clsaa.dop.server.test.model.po.CaseGroup;
import com.clsaa.dop.server.test.service.common.UpdateService;
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
public class CaseGroupUpdateService implements UpdateService<CaseGroupDto> {

    @Autowired
    private CaseGroupDtoMapper caseGroupDtoMapper;

    @Autowired
    private CaseGroupRepository caseGroupRepository;

    @Override
    public void update(CaseGroupDto caseGroupDto) {
        caseGroupDtoMapper.inverseConvert(caseGroupDto)
                .ifPresent(caseGroup -> caseGroupRepository.saveAndFlush(caseGroup));
    }

    @Override
    public void batchUpdate(List<CaseGroupDto> caseGroupDtos) {
        List<CaseGroup> caseGroups = caseGroupDtoMapper.inverseConvert(caseGroupDtos);
        caseGroups.forEach(caseGroup -> caseGroupRepository.saveAndFlush(caseGroup));
    }
}
