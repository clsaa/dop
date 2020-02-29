package com.clsaa.dop.server.test.doExecute.consumer;

import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;

/**
 * @author xihao
 * @version 1.0
 * @since 08/05/2019
 */
@FunctionalInterface
public interface LogConsumer {

    void consume(InterfaceExecuteLog interfaceExecuteLog);
}
