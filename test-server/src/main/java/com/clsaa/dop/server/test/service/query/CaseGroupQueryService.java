package com.clsaa.dop.server.test.service.query;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.dto.CaseGroupDto;
import com.clsaa.dop.server.test.model.po.CaseGroup;
import com.clsaa.dop.server.test.service.common.CommonQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@Component
public class CaseGroupQueryService extends CommonQueryServiceImpl<CaseGroup, CaseGroupDto,Long> {

    @Autowired
    public CaseGroupQueryService(ServiceMapper<CaseGroup, CaseGroupDto> serviceMapper,
                                 JpaRepository<CaseGroup, Long> jpaRepository) {
        super(serviceMapper, jpaRepository);
    }
}
