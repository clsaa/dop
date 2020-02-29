package com.clsaa.dop.server.test.doExecute.consumer;

import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import com.clsaa.dop.server.test.util.ExecutionLogUtils;

/**
 * @author xihao
 * @version 1.0
 * @since 08/05/2019
 */
public class SingleCaseLogConsumer implements LogConsumer {

    @Override
    public void consume(InterfaceExecuteLog interfaceExecuteLog) {
        ExecutionLogUtils.saveLog(interfaceExecuteLog);
    }
}
