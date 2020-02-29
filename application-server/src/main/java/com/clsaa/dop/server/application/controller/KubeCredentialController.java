package com.clsaa.dop.server.application.controller;


import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.vo.ClusterInfoV1;
import com.clsaa.dop.server.application.model.vo.ClusterUrlV1;
import com.clsaa.dop.server.application.model.vo.KubeCredentialWithTokenV1;
import com.clsaa.dop.server.application.service.KubeCredentialService;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.dop.server.application.util.Validator;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * <p>
 * 集群信息接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-25
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class KubeCredentialController {
    @Autowired
    KubeCredentialService kubeCredentialService;

    @ApiOperation(value = "更新集群信息", notes = "更新集群信息")
    @PostMapping(value = "/app/env/{appEnvId}/cluster")
    public void updateUrlAndToken(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "clusterInfoV1", name = "clusterInfoV1", required = true) @RequestBody ClusterInfoV1 clusterInfoV1) {
        BizAssert.validParam(Validator.isUrl(clusterInfoV1.getTargetClusterUrl()),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "集群地址格式错误"));
        this.kubeCredentialService.updateClusterInfo(loginUser, appEnvId, clusterInfoV1.getTargetClusterUrl(), clusterInfoV1.getTargetClusterToken());

    }

    @ApiOperation(value = "查询集群url", notes = "查询集群url")
    @GetMapping(value = "/app/env/{appEnvId}/clusterUrl")
    public ClusterUrlV1 getUrlByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {

        return BeanUtils.convertType(this.kubeCredentialService.findByAppEnvId(loginUser, appEnvId), ClusterUrlV1.class);

    }

    @ApiOperation(value = "查询集群url和Token", notes = "查询集群url和Token")
    @GetMapping(value = "/app/env/{appEnvId}/clusterWithToken")
    public KubeCredentialWithTokenV1 getUrlAndTokenByAppEnvId(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {

        return BeanUtils.convertType(this.kubeCredentialService.queryByAppEnvId(appEnvId), KubeCredentialWithTokenV1.class);

    }

}
