package com.clsaa.dop.server.test.service.create;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.param.InterfaceStageParam;
import com.clsaa.dop.server.test.model.po.InterfaceStage;
import com.clsaa.dop.server.test.service.common.CommonCreateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 19/03/2019
 */
@Component
public class InterfaceStageCreateService extends CommonCreateServiceImpl<InterfaceStageParam, InterfaceStage,Long> {

    @Autowired
    public InterfaceStageCreateService(ServiceMapper<InterfaceStageParam, InterfaceStage> serviceMapper, JpaRepository<InterfaceStage, Long> repository) {
        super(serviceMapper, repository);
    }
}
