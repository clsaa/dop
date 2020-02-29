package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.RequestCheckPointParam;
import com.clsaa.dop.server.test.model.po.RequestCheckPoint;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class CheckPointPoMapper extends AbstractCommonServiceMapper<RequestCheckPointParam, RequestCheckPoint> {

    @Override
    public Class<RequestCheckPointParam> getSourceClass() {
        return RequestCheckPointParam.class;
    }

    @Override
    public Class<RequestCheckPoint> getTargetClass() {
        return RequestCheckPoint.class;
    }

    @Override
    public Optional<RequestCheckPoint> convert(RequestCheckPointParam requestCheckPointParam) {
        return super.convert(requestCheckPointParam).map(dateAndUser());
    }
}
