package com.clsaa.dop.server.test.service.update;

import com.clsaa.dop.server.test.mapper.po2dto.CaseParamDtoMapper;
import com.clsaa.dop.server.test.model.param.update.UpdatedInterfaceCase;
import com.clsaa.dop.server.test.model.po.CaseParam;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.service.common.CommonUpdateServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 16/04/2019
 */
@Component
public class InterfaceCaseUpdateService extends CommonUpdateServiceImpl<Long, UpdatedInterfaceCase, InterfaceCase>{

    @Autowired
    private CaseParamDtoMapper caseParamDtoMapper;

    @Autowired
    public InterfaceCaseUpdateService(JpaRepository<InterfaceCase, Long> repository) {
        super(repository);
    }

    @Override
    protected InterfaceCase doUpdate(InterfaceCase old, UpdatedInterfaceCase param) {
        BeanUtils.copyProperties(param, old);
        List<CaseParam> newParams = caseParamDtoMapper.inverseConvert(param.getCaseParams());
        old.setCaseParams(newParams);
        return old;
    }
}
