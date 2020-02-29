package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.RequestCheckPointDto;
import com.clsaa.dop.server.test.model.po.RequestCheckPoint;
import com.clsaa.dop.server.test.model.po.RequestHeader;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class RequestCheckPointDtoMapper extends AbstractCommonServiceMapper<RequestCheckPoint, RequestCheckPointDto> {

    @Override
    public Class<RequestCheckPoint> getSourceClass() {
        return RequestCheckPoint.class;
    }

    @Override
    public Class<RequestCheckPointDto> getTargetClass() {
        return RequestCheckPointDto.class;
    }

    @Override
    public Optional<RequestCheckPoint> inverseConvert(RequestCheckPointDto requestCheckPointDto) {
        return super.inverseConvert(requestCheckPointDto).map(UserManager.newInfoIfNotExists());
    }

}
