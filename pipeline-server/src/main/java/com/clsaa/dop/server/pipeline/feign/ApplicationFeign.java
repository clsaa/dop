package com.clsaa.dop.server.pipeline.feign;


import com.clsaa.dop.server.pipeline.config.FeignConfig;
import com.clsaa.dop.server.pipeline.config.HttpHeadersConfig;
import com.clsaa.dop.server.pipeline.model.dto.AppBasicInfoV1;
import com.clsaa.dop.server.pipeline.model.dto.KubeCredentialWithTokenV1;
import com.clsaa.dop.server.pipeline.model.dto.LogInfoV1;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(value = "application-server", configuration = FeignConfig.class)
public interface ApplicationFeign {

    /**
     * 根据id查询应用信息
     */
    @ApiOperation(value = "根据ID查询应用信息", notes = "根据ID查询应用项目")
    @GetMapping(value = "/app/{appId}/urlInfo")
    AppBasicInfoV1 findAppById(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(name = "appId", value = "appId", required = true) @PathVariable(value = "appId") Long appId);


    /**
     * 拿版本号
     * */
    @GetMapping(value = "/app/env/{appEnvId}/build_tag")
    String findBuildTagByAppEnvIdAndRunningId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "runningId", name = "运行ID", required = true) @RequestParam(value = "runningId") String runningId);

    /**
     * 拿yaml
     * */
    @GetMapping(value = "/app/env/{appEnvId}/yamlFile")
    String createYamlFileForDeploy(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "runningId", name = "runningId", required = true) @RequestParam(value = "runningId") String runningId);

    /**
     * 查询集群url和Token
     * */
    @GetMapping(value = "/app/env/{appEnvId}/clusterWithToken")
    KubeCredentialWithTokenV1 getUrlAndTokenByAppEnvId(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId);

    /**
     * 添加日志
     * */
    @PostMapping("/app/env/{appEnvId}/log")
    void addLog(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @PathVariable(value = "appEnvId") Long appEnvId,
            @RequestBody LogInfoV1 logInfoV1
    );
}
