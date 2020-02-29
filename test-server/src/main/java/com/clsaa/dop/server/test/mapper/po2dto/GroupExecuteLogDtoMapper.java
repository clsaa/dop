package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.GroupExecuteLogDto;
import com.clsaa.dop.server.test.model.dto.InterfaceExecuteLogDto;
import com.clsaa.dop.server.test.model.po.GroupExecuteLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 08/05/2019
 */
@Component
public class GroupExecuteLogDtoMapper extends AbstractCommonServiceMapper<GroupExecuteLog, GroupExecuteLogDto> {

    @Autowired
    private InterfaceExecuteLogDtoMapper interfaceExecuteLogDtoMapper;

    @Override
    public Class<GroupExecuteLog> getSourceClass() {
        return GroupExecuteLog.class;
    }

    @Override
    public Class<GroupExecuteLogDto> getTargetClass() {
        return GroupExecuteLogDto.class;
    }

    @Override
    public Optional<GroupExecuteLogDto> convert(GroupExecuteLog groupExecuteLog) {
        return super.convert(groupExecuteLog).map(dto ->{
            List<InterfaceExecuteLogDto> caseLogs = interfaceExecuteLogDtoMapper.convert(groupExecuteLog.getLogs());
            dto.setLogs(caseLogs);

            Long cuserId = groupExecuteLog.getCuser();
            dto.setCreateUserName(UserManager.getUserName(cuserId));
            return dto;
        });
    }
}
