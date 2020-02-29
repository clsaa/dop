package com.clsaa.dop.server.test.util;

import com.clsaa.dop.server.test.dao.GroupLogRepository;
import com.clsaa.dop.server.test.dao.InterfaceCaseLogRepository;
import com.clsaa.dop.server.test.model.po.GroupExecuteLog;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;

/**
 * @author xihao
 * @version 1.0
 * @since 30/03/2019
 */
public class ExecutionLogUtils {

    private static InterfaceCaseLogRepository logRepository;

    private static GroupLogRepository groupLogRepository;

    static {
        logRepository = Services.of(InterfaceCaseLogRepository.class);
        groupLogRepository = Services.of(GroupLogRepository.class);
    }

    public static void saveLog(InterfaceExecuteLog interfaceExecuteLog) {
        logRepository.saveAndFlush(interfaceExecuteLog);
    }

    public static GroupExecuteLog saveGroupLog(GroupExecuteLog groupExecuteLog) {
        return groupLogRepository.save(groupExecuteLog);
    }
}
