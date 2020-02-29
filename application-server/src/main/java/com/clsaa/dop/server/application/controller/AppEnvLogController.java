package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.vo.AppEnvLogV1;
import com.clsaa.dop.server.application.model.vo.LogInfoV1;
import com.clsaa.dop.server.application.service.AppEnvLogService;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * <p>
 * 日志服务API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-7
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class AppEnvLogController {
    @Autowired
    private AppEnvLogService appEnvLogService;


    @ApiOperation(value = "查询日志", notes = "查询环境的日志")
    @GetMapping("/app/env/{appEnvId}/log")
    public Pagination<AppEnvLogV1> findLogByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(name = "pageNo", value = "页号", required = true, defaultValue = "1") @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @ApiParam(name = "pageSize", value = "页大小", required = true, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @ApiParam(name = "appEnvId", value = "应用环境Id", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        return this.appEnvLogService.getLogByAppEnvId(loginUser, pageNo, pageSize, appEnvId);

    }


    @ApiOperation(value = "添加日志", notes = "添加日志")
    @PostMapping("/app/env/{appEnvId}/log")
    public void addLog(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @PathVariable(value = "appEnvId") Long appEnvId,
            @RequestBody LogInfoV1 logInfoV1) throws Exception {
        this.appEnvLogService.addLog(loginUser, logInfoV1, appEnvId);
    }
}
