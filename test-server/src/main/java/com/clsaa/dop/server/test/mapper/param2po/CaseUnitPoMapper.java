package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.CaseUnitParam;
import com.clsaa.dop.server.test.model.po.CaseGroup;
import com.clsaa.dop.server.test.model.po.CaseUnit;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@Component
public class CaseUnitPoMapper extends AbstractCommonServiceMapper<CaseUnitParam,CaseUnit> {

    @Override
    public Class<CaseUnitParam> getSourceClass() {
        return CaseUnitParam.class;
    }

    @Override
    public Class<CaseUnit> getTargetClass() {
        return CaseUnit.class;
    }

    @Override
    public Optional<CaseUnit> convert(CaseUnitParam caseUnitParam) {
        return super.convert(caseUnitParam)
                .map(dateAndUser());
    }
}
