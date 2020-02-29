package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.RequestResultParam;
import com.clsaa.dop.server.test.model.po.UrlResultParam;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class ResultParamPoMapper extends AbstractCommonServiceMapper<RequestResultParam, UrlResultParam> {

    @Override
    public Class<RequestResultParam> getSourceClass() {
        return RequestResultParam.class;
    }

    @Override
    public Class<UrlResultParam> getTargetClass() {
        return UrlResultParam.class;
    }

    @Override
    public Optional<UrlResultParam> convert(RequestResultParam requestResultParam) {
        return super.convert(requestResultParam).map(dateAndUser());
    }
}
