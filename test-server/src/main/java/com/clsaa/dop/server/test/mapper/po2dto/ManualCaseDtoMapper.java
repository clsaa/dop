package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.model.po.ManualCase;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class ManualCaseDtoMapper extends AbstractCommonServiceMapper<ManualCase, ManualCaseDto> {

    @Override
    public Class<ManualCase> getSourceClass() {
        return ManualCase.class;
    }

    @Override
    public Class<ManualCaseDto> getTargetClass() {
        return ManualCaseDto.class;
    }
}
