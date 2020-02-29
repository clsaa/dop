package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.OperationExecuteLogDto;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import org.springframework.stereotype.Component;

/**
 * @author xihao
 * @version 1.0
 * @since 31/03/2019
 */
@Component
public class OperationLogDtoMapper extends AbstractCommonServiceMapper<OperationExecuteLog, OperationExecuteLogDto> {

    @Override
    public Class<OperationExecuteLog> getSourceClass() {
        return OperationExecuteLog.class;
    }

    @Override
    public Class<OperationExecuteLogDto> getTargetClass() {
        return OperationExecuteLogDto.class;
    }
}
