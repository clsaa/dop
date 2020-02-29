package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.vo.DeploymentYamlV1;
import com.clsaa.dop.server.application.model.vo.KubeYamlDataV1;
import com.clsaa.dop.server.application.service.KubeYamlService;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.dop.server.application.util.Validator;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.HashMap;
import java.util.List;


/**
 * <p>
 * KuberneteYamlAPI接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-25
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class KubeYamlController {
    @Autowired
    KubeYamlService kubeYamlService;

    @ApiOperation(value = "获取传输的yaml文件", notes = "获取传输的yaml文件")
    @GetMapping(value = "/app/env/{appEnvId}/yamlFile")
    public String createYamlFileForDeploy(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            //@ApiParam(value = "cuser", name = "cuser", required = true) @RequestParam(value = "cuser") Long cuser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "runningId", name = "runningId", required = true) @RequestParam(value = "runningId") String runningId) {
        try {
            return this.kubeYamlService.createYamlFileForDeploy(cuser, appEnvId, runningId);

        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    //@ApiOperation(value = "获取传输的yaml文件", notes = "获取传输的yaml文件")
    //@GetMapping(value = "/app/env/{appEnvId}/test")
    //public String test(
    //        //@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
    //        //@ApiParam(value = "cuser", name = "cuser", required = true) @RequestParam(value = "cuser") Long cuser,
    //        @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId)
    //        //@ApiParam(value = "runningId", name = "runningId", required = true) @RequestParam(value = "runningId") String runningId) {{
    //{
    //    try {
    //        return this.kubeYamlService.test(appEnvId);
    //
    //    } catch (Exception e) {
    //        System.out.print(e);
    //        return null;
    //    }
    //}

    @ApiOperation(value = "创建yaml信息", notes = "创建yaml信息")
    @PostMapping(value = "/app/env/{appEnvId}/yaml")
    public void CreateYamlInfoByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy,
            @ApiParam(name = "nameSpace", value = "命名空间", defaultValue = "") @RequestParam(value = "nameSpace", defaultValue = "") String nameSpace,
            @ApiParam(name = "service", value = "服务", defaultValue = "") @RequestParam(value = "service", defaultValue = "") String service,
            @ApiParam(name = "deployment", value = "部署", defaultValue = "") @RequestParam(value = "deployment", defaultValue = "") String deployment,
            @ApiParam(name = "containers", value = "容器", defaultValue = "") @RequestParam(value = "containers", defaultValue = "") String containers,
            @ApiParam(name = "releaseStrategy", value = "发布策略", required = true) @RequestParam(value = "releaseStrategy") String releaseStrategy,
            @ApiParam(name = "replicas", value = "副本数量", defaultValue = "0") @RequestParam(value = "replicas", defaultValue = "0") Integer replicas,
            @ApiParam(name = "releaseBatch", value = "发布批次", defaultValue = "0") @RequestParam(value = "releaseBatch", defaultValue = "0") Long releaseBatch,
            @ApiParam(name = "yamlFilePath", value = "镜像地址", defaultValue = "") @RequestParam(value = "yamlFilePath", defaultValue = "") String yamlFilePath) throws Exception {
        this.kubeYamlService.CreateYamlData(appEnvId, loginUser, nameSpace, service, deployment, containers, releaseStrategy, replicas
                , releaseBatch, yamlFilePath);
    }

    @ApiOperation(value = "更新yaml信息", notes = "更新yaml信息")
    @PutMapping(value = "/app/env/{appEnvId}/yaml")
    public void updateYamlInfoByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy,
            @ApiParam(name = "nameSpace", value = "命名空间", defaultValue = "") @RequestParam(value = "nameSpace", defaultValue = "") String nameSpace,
            @ApiParam(name = "service", value = "服务", defaultValue = "") @RequestParam(value = "service", defaultValue = "") String service,
            @ApiParam(name = "deployment", value = "部署", defaultValue = "") @RequestParam(value = "deployment", defaultValue = "") String deployment,
            @ApiParam(name = "containers", value = "容器", defaultValue = "") @RequestParam(value = "containers", defaultValue = "") String containers,
            @ApiParam(name = "releaseStrategy", value = "发布策略", required = true) @RequestParam(value = "releaseStrategy") String releaseStrategy,
            @ApiParam(name = "replicas", value = "副本数量", defaultValue = "0") @RequestParam(value = "replicas", defaultValue = "0") Integer replicas,
            @ApiParam(name = "releaseBatch", value = "发布批次", defaultValue = "0") @RequestParam(value = "releaseBatch", defaultValue = "0") Long releaseBatch,
            @ApiParam(name = "yamlFilePath", value = "yaml文件地址", defaultValue = "") @RequestParam(value = "yamlFilePath", defaultValue = "") String yamlFilePath) throws Exception {
        BizAssert.validParam(Validator.isServiceName(service),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "服务名格式错误"));
        if (!yamlFilePath.equals("")) {
            BizAssert.validParam(Validator.isRelativePath(yamlFilePath),
                    new BizCode(BizCodes.INVALID_PARAM.getCode(), "相对路径格式错误"));
        }
        this.kubeYamlService.updateYamlData(appEnvId, loginUser, nameSpace, service, deployment, containers, releaseStrategy, replicas
                , releaseBatch, yamlFilePath);
    }

    @ApiOperation(value = "获取yaml信息", notes = "获取yaml信息")
    @GetMapping(value = "/app/env/{appEnvId}/yaml")

    public KubeYamlDataV1 getYamlInfoByAppEnvId(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
                                                @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        return BeanUtils.convertType(this.kubeYamlService.findYamlDataByEnvId(loginUser, appEnvId), KubeYamlDataV1.class);
    }

    @ApiOperation(value = "更新yaml文件", notes = "更新yaml文件")
    @PutMapping(value = "/app/env/{appEnvId}/deploymentYaml")
    public void updateDeploymentYaml(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "deploymentYaml", name = "deploymentYaml", required = true) @RequestBody DeploymentYamlV1 deploymentYamlV1) {
        this.kubeYamlService.updateDeploymentYaml(loginUser, appEnvId, deploymentYamlV1.getDeploymentEditableYaml());
    }


    @ApiOperation(value = "判断yaml是否存在", notes = "判断yaml是否存在")
    @GetMapping(value = "/app/env/{appEnvId}/yamlStatus")
    public Boolean isExistYamlData(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        return this.kubeYamlService.isExistYamlData(appEnvId);
    }


    @ApiOperation(value = "获取命名空间", notes = "获取命名空间")
    @GetMapping(value = "/app/env/{appEnvId}/cluster/allNamespaces")
    public List<String> getNameSpaceByUrlAndToken(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId
    ) {
        try {
            return this.kubeYamlService.findNameSpaces(loginUser, appEnvId);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "获取服务", notes = "获取服务")
    @GetMapping(value = "/app/env/{appEnvId}/cluster/allServices")
    public List<String> getServiceByNameSpace(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace
    ) {
        try {
            return this.kubeYamlService.getServiceByNameSpace(loginUser, appEnvId, namespace);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "获取部署", notes = "获取部署")
    @GetMapping(value = "/app/env/{appEnvId}/cluster/allDeployment")
    public HashMap<String, Object> getDeploymentByNameSpaceAndService(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace,
            @ApiParam(value = "service", name = "service", required = true) @RequestParam(value = "service") String service
    ) {

        try {
            return this.kubeYamlService.getDeploymentByNameSpaceAndService(loginUser, appEnvId, namespace, service);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "创建服务", notes = "创建服务")
    @PostMapping(value = "/app/env/{appEnvId}/cluster/service")
    public void createServiceByNameSpace(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace,
            @ApiParam(value = "name", name = "name", required = true) @RequestParam(value = "name") String name,
            @ApiParam(value = "targetPort", name = "targetPort", required = true) @RequestParam(value = "targetPort") Integer targetPort,
            @ApiParam(value = "nodePort", name = "nodePort", defaultValue = "") @RequestParam(value = "nodePort", required = false) Integer nodePort,
            @ApiParam(value = "host", name = "host", defaultValue = "") @RequestParam(value = "host", required = false) String host

    ) {
        BizAssert.validParam(Validator.isServiceName(name),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "服务名格式错误"));
        try {
            this.kubeYamlService.createServiceByNameSpace(loginUser, appEnvId, namespace, name, targetPort, nodePort, host);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

}
