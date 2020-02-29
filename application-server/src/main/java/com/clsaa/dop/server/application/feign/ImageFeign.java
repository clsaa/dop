package com.clsaa.dop.server.application.feign;

import com.clsaa.dop.server.application.config.FeignConfig;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "image-server", configuration = FeignConfig.class)
public interface ImageFeign {
    @ApiOperation(value = "新建项目", notes = "需要给出项目的基本信息，返回http状态码")
    @PostMapping(value = "/v1/projects")
    public void addProject(@ApiParam(value = "项目名称", required = true) @RequestParam(value = "name") String projectName,
                           @ApiParam(value = "项目权限", required = true) @RequestParam(value = "status") String publicStatus,
                           @ApiParam(value = "用户id", required = true) @RequestHeader(value = "x-login-user") Long userId);

    @ApiOperation(value = "通过项目名称获取镜像仓库地址")
    @GetMapping(value = "/v1/repoAddress")
    public List<String> getRepoAddress(@ApiParam(value = "用户id") @RequestHeader(value = "x-login-user") Long userId);
}
