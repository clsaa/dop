package com.clsaa.dop.server.test.service.create;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.param.CaseGroupParam;
import com.clsaa.dop.server.test.model.po.CaseGroup;
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
public class CaseGroupCreateService extends CommonCreateServiceImpl<CaseGroupParam, CaseGroup, Long> {

    @Autowired
    public CaseGroupCreateService(ServiceMapper<CaseGroupParam, CaseGroup> serviceMapper,
                                  JpaRepository<CaseGroup, Long> repository) {
        super(serviceMapper, repository);
    }
}
