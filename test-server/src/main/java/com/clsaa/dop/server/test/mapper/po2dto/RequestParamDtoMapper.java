package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.RequestParamDto;
import com.clsaa.dop.server.test.model.po.RequestParam;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Component
public class RequestParamDtoMapper extends AbstractCommonServiceMapper<RequestParam, RequestParamDto> {

    @Override
    public Class<RequestParam> getSourceClass() {
        return RequestParam.class;
    }

    @Override
    public Class<RequestParamDto> getTargetClass() {
        return RequestParamDto.class;
    }

    @Override
    public Optional<RequestParam> inverseConvert(RequestParamDto requestParamDto) {
        return super.inverseConvert(requestParamDto)
                .map(UserManager.newInfoIfNotExists());
    }
}
