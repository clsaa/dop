package com.clsaa.dop.server.test.service.create;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.param.CaseUnitParam;
import com.clsaa.dop.server.test.model.po.CaseUnit;
import com.clsaa.dop.server.test.service.common.CommonCreateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@Component
public class CaseUnitCreateService extends CommonCreateServiceImpl<CaseUnitParam, CaseUnit,Long> {

    @Autowired
    public CaseUnitCreateService(ServiceMapper<CaseUnitParam, CaseUnit> serviceMapper,
                                 JpaRepository<CaseUnit, Long> repository) {
        super(serviceMapper, repository);
    }
}
