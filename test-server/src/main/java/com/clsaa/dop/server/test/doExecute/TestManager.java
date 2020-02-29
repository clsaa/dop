package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.doExecute.context.RequestContext;
import com.clsaa.dop.server.test.doExecute.consumer.GroupLogConsumer;
import com.clsaa.dop.server.test.doExecute.consumer.SingleCaseLogConsumer;
import com.clsaa.dop.server.test.doExecute.plugin.PluginManager;
import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.model.dto.CaseGroupDto;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.po.GroupExecuteLog;
import com.clsaa.dop.server.test.util.ExecutionLogUtils;
import com.clsaa.dop.server.test.util.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 完成测试脚本的执行
 * @author xihao
 * @version 1.0
 * @since 20/03/2019
 */
public class TestManager {

    public static final String FAIL_RESULT = "fail";
    public static final String SUCCESS_RESULT = "success";

    public static final int MAX_TASK_SIZE = 1000;

    private static PluginManager pluginManager;

    private static BlockingQueue<Runnable> tasksHolder;
    private static ThreadPoolExecutor testScriptExecutor;

    static {
        pluginManager = Services.of(PluginManager.class);
        tasksHolder = new ArrayBlockingQueue<>(MAX_TASK_SIZE, false);
        testScriptExecutor = new ThreadPoolExecutor(
                3,
                5,
                5,
                TimeUnit.MINUTES,
                tasksHolder
        );
    }

    public static InterfaceCaseDto execute(InterfaceCaseDto interfaceCaseDto) {
        Long userId = UserManager.getCurrentUserId();
        ExecuteTask executeTask = new ExecuteTask(interfaceCaseDto, userId, new SingleCaseLogConsumer());
        testScriptExecutor.execute(executeTask);
        return interfaceCaseDto;
    }

    public static Boolean execute(CaseGroupDto caseGroupDto, List<InterfaceCaseDto> interfaceCases) {
        LocalDateTime current = LocalDateTime.now();
        Long user = UserManager.getCurrentUserId();
        GroupExecuteLog groupExecuteLog = GroupExecuteLog.builder()
                .groupId(caseGroupDto.getId())
                .ctime(current)
                .mtime(current)
                .cuser(user)
                .muser(user)
                .deleted(false)
                .build();
        GroupExecuteLog savedGroup = ExecutionLogUtils.saveGroupLog(groupExecuteLog);
        Long groupLogId = savedGroup.getId();
        interfaceCases.forEach(interfaceCase -> {
            ExecuteTask executeTask = new ExecuteTask(
                    interfaceCase, user, caseGroupDto, savedGroup, new GroupLogConsumer(groupLogId)
            );
            testScriptExecutor.execute(executeTask);
        });
        return true;
    }

    public static String urlHandled(RequestContext requestContext) {
        return pluginManager.url(requestContext);
    }

    public static Map<String, String> requestHeadersHandled(RequestContext requestContext) {
        return pluginManager.headers(requestContext);
    }

}

