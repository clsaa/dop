package com.clsaa.dop.server.test.service.create;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.param.CaseParamRef;
import com.clsaa.dop.server.test.model.po.CaseParam;
import com.clsaa.dop.server.test.service.common.CommonCreateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 11/04/2019
 */
@Component
public class CaseParamCreateService extends CommonCreateServiceImpl<CaseParamRef, CaseParam, Long> {

    @Autowired
    public CaseParamCreateService(ServiceMapper<CaseParamRef, CaseParam> serviceMapper, JpaRepository<CaseParam, Long> repository) {
        super(serviceMapper, repository);
    }
}
