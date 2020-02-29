package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.doExecute.context.ExecuteContext;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import com.clsaa.dop.server.test.manager.UserManager;

import java.time.LocalDateTime;
import java.util.Comparator;

import static com.clsaa.dop.server.test.doExecute.TestManager.SUCCESS_RESULT;

public interface Operation {

    OperationType type();

    int order();

    void run(ExecuteContext executeContext);

    String result();

    static Comparator<Operation> operationSorter() {
        return Comparator.comparingInt(Operation::order);
    }

    default OperationExecuteLog initOperationLog(ExecuteContext executeContext) {
        return (OperationExecuteLog) UserManager.dateAndUser().apply(
                OperationExecuteLog.builder()
                        .interfaceExecuteLog(executeContext.getInterfaceExecuteLog())
                        .stage(executeContext.getCurrentStage())
                        .begin(LocalDateTime.now())
                        .operationType(type())
                        .order(order())
                        .build()
        );
    }

    default OperationExecuteLog endOperationLog(OperationExecuteLog executeLog) {
        executeLog.setEnd(LocalDateTime.now());
        executeLog.setSuccess(SUCCESS_RESULT.equals(result()));
        return executeLog;
    }
}
