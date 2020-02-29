package com.clsaa.dop.server.test.doExecute.context;

import com.clsaa.dop.server.test.enums.Stage;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * @author xihao
 * @version 1.0
 * @since 30/03/2019
 */
@Data
@Builder
public class ExecuteContext {

    private InterfaceExecuteLog interfaceExecuteLog;

    private InterfaceCaseDto interfaceCaseDto;

    private Stage currentStage;

    private Map<String, String> caseParams;

    public InterfaceExecuteLog logAfterExecution() {
        interfaceExecuteLog.setEnd(LocalDateTime.now());
        interfaceExecuteLog.setSuccess(interfaceCaseDto.executeSuccess());
        return interfaceExecuteLog;
    }

    public void addOperationLog(OperationExecuteLog operationExecuteLog) {
        if (nonNull(interfaceExecuteLog)
                && nonNull(operationExecuteLog)
                && nonNull(interfaceExecuteLog.getOperationExecuteLogs())) {
            interfaceExecuteLog.getOperationExecuteLogs().add(operationExecuteLog);
        }
    }

    public void addParam(String key, String value) {
        if (nonNull(caseParams)) {
            caseParams.put(key, value);
        }
    }
}
