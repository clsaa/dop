package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.CaseGroupParam;
import com.clsaa.dop.server.test.model.po.CaseGroup;
import com.clsaa.dop.server.test.model.po.CaseUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@Component
public class CaseGroupPoMapper extends AbstractCommonServiceMapper<CaseGroupParam, CaseGroup> {

    @Autowired
    private CaseUnitPoMapper caseUnitPoMapper;

    @Override
    public Class<CaseGroupParam> getSourceClass() {
        return CaseGroupParam.class;
    }

    @Override
    public Class<CaseGroup> getTargetClass() {
        return CaseGroup.class;
    }

    @Override
    public Optional<CaseGroup> convert(CaseGroupParam param) {
        return super.convert(param)
                .map(UserManager.dateAndUser())
                .map(group -> {
                    List<CaseUnit> caseUnits = caseUnitPoMapper.convert(param.getCaseUnits());
                    caseUnits.forEach(unit -> unit.setCaseGroup(group));
                    group.setCaseUnits(caseUnits);
                    return group;
                })
                ;
    }
}
