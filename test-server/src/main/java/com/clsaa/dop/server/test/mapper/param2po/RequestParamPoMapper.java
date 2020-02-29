package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.RequestParamCreateParam;
import com.clsaa.dop.server.test.model.po.RequestParam;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Component
public class RequestParamPoMapper extends AbstractCommonServiceMapper<RequestParamCreateParam, RequestParam> {

    @Override
    public Class<RequestParamCreateParam> getSourceClass() {
        return RequestParamCreateParam.class;
    }

    @Override
    public Class<RequestParam> getTargetClass() {
        return RequestParam.class;
    }

    @Override
    public Optional<RequestParam> convert(RequestParamCreateParam requestParamCreateParam) {
        return super.convert(requestParamCreateParam).map(UserManager.dateAndUser());
    }
}
