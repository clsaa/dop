package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.RequestHeaderParam;
import com.clsaa.dop.server.test.model.po.RequestHeader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

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
        return super.convert(requestHeaderParam).map(po -> {
            LocalDateTime current = LocalDateTime.now();
            po.setCtime(current);
            po.setMtime(current);
            //todo set user
            po.setCuser(110L);
            po.setMuser(110L);
            return po;
        });
    }
}
