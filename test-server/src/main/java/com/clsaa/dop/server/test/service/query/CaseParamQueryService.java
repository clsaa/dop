package com.clsaa.dop.server.test.service.query;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.dto.CaseParamDto;
import com.clsaa.dop.server.test.model.po.CaseParam;
import com.clsaa.dop.server.test.service.common.CommonQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 20/04/2019
 */
@Component
public class CaseParamQueryService extends CommonQueryServiceImpl<CaseParam, CaseParamDto,Long> {

    @Autowired
    public CaseParamQueryService(ServiceMapper<CaseParam, CaseParamDto> serviceMapper,
                                 JpaRepository<CaseParam, Long> jpaRepository) {
        super(serviceMapper, jpaRepository);
    }

}
