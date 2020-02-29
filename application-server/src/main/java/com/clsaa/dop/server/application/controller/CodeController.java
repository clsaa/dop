package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.service.CodeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.List;

/**
 * <p>
 * 代码API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-12
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class CodeController {

    @Autowired
    private CodeService codeService;

    @ApiOperation(value = "查询代码仓库地址", notes = "查询代码仓库地址")
    @GetMapping(value = "/git_url_list")
    public List<String> findGitUrls(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser) {
        return this.codeService.findProjectUrlList(loginUser);
    }
}
