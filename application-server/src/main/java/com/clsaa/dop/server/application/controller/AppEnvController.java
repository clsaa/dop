package com.clsaa.dop.server.application.controller;


import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.vo.AppEnvV1;
import com.clsaa.dop.server.application.model.vo.PipelineIdAndNameV1;
import com.clsaa.dop.server.application.service.AppEnvService;
import com.clsaa.dop.server.application.service.BuildTagRunningIdMappingService;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用环境API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-16
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class AppEnvController {
    @Autowired
    AppEnvService appEnvService;

    @Autowired
    BuildTagRunningIdMappingService buildTagRunningIdMappingService;

    @ApiOperation(value = "查询应用环境信息", notes = "根据应用ID查询应用环境信息")
    @GetMapping(value = "/app/{appId}/allEnv")
    public List<AppEnvV1> findEnvironmentByAppId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appId", name = "应用ID", required = true) @PathVariable(value = "appId") Long appId) {
        return this.appEnvService.findEnvironmentByAppId(loginUser, appId).stream().map(l -> BeanUtils.convertType(l, AppEnvV1.class)).collect(Collectors.toList());
    }

    @ApiOperation(value = "查询应用环境详情", notes = "根据应用环境信息ID查询应用环境详情")
    @GetMapping(value = "/app/env/{appEnvId}")
    public AppEnvV1 findEnvironmentDetailById(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        return BeanUtils.convertType(this.appEnvService.findEnvironmentDetailById(loginUser, appEnvId), AppEnvV1.class);
    }


    @ApiOperation(value = "获取Build_tag", notes = "获取Build_tag")
    @GetMapping(value = "/app/env/{appEnvId}/build_tag")
    public String findBuildTagByAppEnvIdAndRunningId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            //@ApiParam(value = "cuser", name = "cuser", required = true) @RequestParam(value = "cuser") Long cuser,
            @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "runningId", name = "运行ID", required = true) @RequestParam(value = "runningId") String runningId) {
        return this.buildTagRunningIdMappingService.findBuildTagByRunningIdAndAppEnvId(loginUser, runningId, appEnvId);
    }




    @ApiOperation(value = "删除应用环境", notes = "根据应用环境ID删除应用环境")
    @DeleteMapping(value = "/app/env/{appEnvId}")
    public void deleteEnvironmentById(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        this.appEnvService.deleteEnvironmentById(loginUser, appEnvId);
    }

    @ApiOperation(value = "根据环境id查询pipelineId", notes = "根据环境id查询pipelineId")
    @GetMapping(value = "/app/env/{appEnvId}/pipeline")
    public PipelineIdAndNameV1 findPipelineByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        return this.appEnvService.findPipelineByAppEnvId(loginUser, appEnvId);
    }
    //@ApiOperation(value = "更新Build_Tag", notes = "更新Build_Tag")
    //@PutMapping(value = "/app/env/{appEnvId}/build_tag")
    //public String updateBuildTag(
    //        @PathVariable(value = "appEnvId") Long appEnvId
    //){
    //    return this.appEnvService.updateBuildTag(appEnvId);
    //
    //}

    @ApiOperation(value = "根据应用id创建应用环境", notes = "根据应用id创建应用环境")
    @PostMapping(value = "/app/{appId}/env")
    public void createEnvironmentByAppId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appId", name = "appId", required = true) @PathVariable(value = "appId") Long appId,
            @ApiParam(name = "title", value = "环境名称", required = true) @RequestParam(value = "title") String title,
            @ApiParam(name = "environmentLevel", value = "环境级别", required = true) @RequestParam(value = "environmentLevel") String environmentLevel,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy) {
        BizAssert.validParam(StringUtils.hasText(title) && title.length() < 25,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "标题长度必须小于25"));
        this.appEnvService.createEnvironmentByAppId(appId, loginUser, title, environmentLevel, deploymentStrategy);
    }


    //@ApiOperation(value = "根据应用环境id查询流水线", notes = "根据应用环境id查询流水线")
    //@GetMapping(value = "/app/env/{appEnvId}/pipeline")
    //public void findPipelineByAppEnvId(
    //        @ApiParam(value = "appEnvId", name = "环境ID", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
    //    this.appEnvService.findPipelineByAppEnvId(appEnvId);
    //}


}
