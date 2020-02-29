package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.RequestHeaderDto;
import com.clsaa.dop.server.test.model.po.RequestHeader;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Handler;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class RequestHeaderDtoMapper extends AbstractCommonServiceMapper<RequestHeader, RequestHeaderDto> {

    @Override
    public Class<RequestHeader> getSourceClass() {
        return RequestHeader.class;
    }

    @Override
    public Class<RequestHeaderDto> getTargetClass() {
        return RequestHeaderDto.class;
    }

    @Override
    public Optional<RequestHeader> inverseConvert(RequestHeaderDto requestHeaderDto) {
        return super.inverseConvert(requestHeaderDto)
                .map(UserManager.newInfoIfNotExists());
    }


}
