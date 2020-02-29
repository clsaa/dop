package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.doExecute.Operation;
import com.clsaa.dop.server.test.doExecute.context.ExecuteContext;
import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.clsaa.dop.server.test.doExecute.TestManager.FAIL_RESULT;
import static com.clsaa.dop.server.test.doExecute.TestManager.SUCCESS_RESULT;
import static com.clsaa.dop.server.test.enums.OperationType.WAIT;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitOperationDto implements Operation {

    private static final Logger log = LoggerFactory.getLogger(WaitOperationDto.class);

    private OperationType operationType;

    /**
     * 单位为毫秒
     */
    private int waitTime;

    private int order;

    private String result = FAIL_RESULT;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

    public static WaitOperationDto emptyWait() {
        return WaitOperationDto.builder()
                .order(-1)
                .build();
    }

    @Override
    public OperationType type() {
        return WAIT;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public void run(ExecuteContext executeContext) {
        OperationExecuteLog executeLog = initOperationLog(executeContext);
        String executeMessage;
        try {
            TimeUnit.MILLISECONDS.sleep(waitTime);
            result = SUCCESS_RESULT;
            executeMessage = "Pause " + waitTime + "ms Successfully!";
        } catch (InterruptedException e) {
            executeMessage = "[Stage Operation] Thread is interrupted while waiting";
            log.error(executeMessage, e);
            result = FAIL_RESULT;
        }
        executeLog.setExecuteInfo(executeMessage);
        executeContext.addOperationLog(endOperationLog(executeLog));
    }

    @Override
    public String result() {
        return result;
    }
}
