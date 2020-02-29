package com.clsaa.dop.server.test.service.query;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.model.po.ManualCase;
import com.clsaa.dop.server.test.service.common.CommonQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 27/03/2019
 */
@Component
public class ManualCaseQueryService extends CommonQueryServiceImpl<ManualCase, ManualCaseDto, Long> {

    @Autowired
    public ManualCaseQueryService(ServiceMapper<ManualCase, ManualCaseDto> serviceMapper, JpaRepository<ManualCase, Long> jpaRepository) {
        super(serviceMapper, jpaRepository);
    }
}
