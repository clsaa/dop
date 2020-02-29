package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.RequestHeaderParam;
import com.clsaa.dop.server.test.model.po.RequestHeader;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class RequestHeaderPoMapper extends AbstractCommonServiceMapper<RequestHeaderParam, RequestHeader> {

    @Override
    public Class<RequestHeaderParam> getSourceClass() {
        return RequestHeaderParam.class;
    }

    @Override
    public Class<RequestHeader> getTargetClass() {
        return RequestHeader.class;
    }

    @Override
    public Optional<RequestHeader> convert(RequestHeaderParam requestHeaderParam) {
        return super.convert(requestHeaderParam).map(dateAndUser());
    }
}
