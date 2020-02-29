package com.clsaa.dop.server.test.service.query;

import com.clsaa.dop.server.test.dao.GroupLogRepository;
import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.dto.GroupExecuteLogDto;
import com.clsaa.dop.server.test.model.po.GroupExecuteLog;
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
 * @since 08/05/2019
 */
@Component
public class GroupLogQueryService extends CommonQueryServiceImpl<GroupExecuteLog, GroupExecuteLogDto,Long> {

    private GroupLogRepository groupLogRepository;

    @Autowired
    public GroupLogQueryService(ServiceMapper<GroupExecuteLog, GroupExecuteLogDto> serviceMapper,
                                GroupLogRepository jpaRepository) {
        super(serviceMapper, jpaRepository);
        this.groupLogRepository = jpaRepository;
    }

    public Pagination<GroupExecuteLogDto> getGroupLogs(Long groupId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<GroupExecuteLog> logs = groupLogRepository.findByGroupIdOrderByCtimeDesc(groupId, pageable);
        return toPagination(pageNo, pageSize).apply(logs);
    }
}
