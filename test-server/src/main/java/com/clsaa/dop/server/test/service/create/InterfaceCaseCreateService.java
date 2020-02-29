package com.clsaa.dop.server.test.service.create;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.param.InterfaceCaseParam;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.service.common.CommonCreateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class InterfaceCaseCreateService extends CommonCreateServiceImpl<InterfaceCaseParam, InterfaceCase, Long> {

    @Autowired
    public InterfaceCaseCreateService(ServiceMapper<InterfaceCaseParam, InterfaceCase> serviceMapper, JpaRepository<InterfaceCase, Long> repository) {
        super(serviceMapper, repository);
    }
}
