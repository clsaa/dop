package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.InterfaceCaseParam;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 19/03/2019
 */
@Component
public class InterfaceCasePoMapper extends AbstractCommonServiceMapper<InterfaceCaseParam, InterfaceCase> {

    @Override
    public Class<InterfaceCaseParam> getSourceClass() {
        return InterfaceCaseParam.class;
    }

    @Override
    public Class<InterfaceCase> getTargetClass() {
        return InterfaceCase.class;
    }

    @Override
    public Optional<InterfaceCase> convert(InterfaceCaseParam param) {
        return super.convert(param).map(po -> {
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
