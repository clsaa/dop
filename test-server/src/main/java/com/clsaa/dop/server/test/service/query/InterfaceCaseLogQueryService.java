package com.clsaa.dop.server.test.service.query;

import com.clsaa.dop.server.test.dao.InterfaceCaseLogRepository;
import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceExecuteLogDto;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import com.clsaa.dop.server.test.service.common.CommonQueryServiceImpl;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 31/03/2019
 */
@Component
public class InterfaceCaseLogQueryService extends CommonQueryServiceImpl<InterfaceExecuteLog, InterfaceExecuteLogDto, Long> {

    private InterfaceCaseLogRepository repository;

    @Autowired
    public InterfaceCaseLogQueryService(ServiceMapper<InterfaceExecuteLog, InterfaceExecuteLogDto> serviceMapper,
                                        InterfaceCaseLogRepository jpaRepository) {
        super(serviceMapper, jpaRepository);
        this.repository = jpaRepository;
    }

    public Pagination<InterfaceExecuteLogDto> getExecuteLogs(Long caseId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<InterfaceExecuteLog> logs = repository.findByCaseIdOrderByBeginDesc(caseId, pageable);
        return toPagination(pageNo, pageSize).apply(logs);
    }

}
