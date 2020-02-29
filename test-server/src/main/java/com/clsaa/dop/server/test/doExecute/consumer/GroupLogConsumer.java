package com.clsaa.dop.server.test.doExecute.consumer;

import com.clsaa.dop.server.test.model.po.GroupExecuteLog;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import com.clsaa.dop.server.test.util.ExecutionLogUtils;

import java.util.Collections;

/**
 * @author xihao
 * @version 1.0
 * @since 08/05/2019
 */
public class GroupLogConsumer implements LogConsumer {

    private Long groupLogId;

    public GroupLogConsumer(Long groupLogId) {
        this.groupLogId = groupLogId;
    }

    public void clear() {
        this.groupLogId = null;
    }

    public void setGroupLogId(Long groupLogId) {
        this.groupLogId = groupLogId;
    }

    @Override
    public void consume(InterfaceExecuteLog interfaceExecuteLog) {
        GroupExecuteLog groupExecuteLog = new GroupExecuteLog();
        groupExecuteLog.setId(groupLogId);
        groupExecuteLog.setLogs(Collections.singletonList(interfaceExecuteLog));
        interfaceExecuteLog.setGroupExecuteLog(groupExecuteLog);
        ExecutionLogUtils.saveLog(interfaceExecuteLog);
    }
}
