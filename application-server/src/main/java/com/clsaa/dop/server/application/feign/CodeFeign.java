package com.clsaa.dop.server.application.feign;

import com.clsaa.dop.server.application.config.FeignConfig;
import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Component
@FeignClient(value = "code-server", configuration = FeignConfig.class)
public interface CodeFeign {
    @ApiOperation(value = "获得用户可以拉取代码的所有项目地址", notes = "根据用户的id获得可以拉取代码的所有项目地址，包括所有的public项目，和权限在guest以上的项目")
    @GetMapping("/project_url_list")
    public List<String> findProjectUrlList(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long userId);
}
