package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.vo.KubeYamlDataV1;
import com.clsaa.dop.server.application.service.KubeYamlService;
import com.clsaa.dop.server.application.util.BeanUtils;
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
    public HashMap<String, String> createYamlFileForDeploy(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        try {
            return this.kubeYamlService.createYamlFileForDeploy(appEnvId);

        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "创建yaml信息", notes = "创建yaml信息")
    @PostMapping(value = "/app/env/{appEnvId}/yaml")
    public void CreateYamlInfoByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy,
            @ApiParam(name = "nameSpace", value = "命名空间", defaultValue = "") @RequestParam(value = "nameSpace", defaultValue = "") String nameSpace,
            @ApiParam(name = "service", value = "服务", defaultValue = "") @RequestParam(value = "service", defaultValue = "") String service,
            @ApiParam(name = "deployment", value = "部署", defaultValue = "") @RequestParam(value = "deployment", defaultValue = "") String deployment,
            @ApiParam(name = "containers", value = "容器", defaultValue = "") @RequestParam(value = "containers", defaultValue = "") String containers,
            @ApiParam(name = "releaseStrategy", value = "发布策略", required = true) @RequestParam(value = "releaseStrategy") String releaseStrategy,
            @ApiParam(name = "replicas", value = "副本数量", defaultValue = "0") @RequestParam(value = "replicas", defaultValue = "0") Integer replicas,
            @ApiParam(name = "releaseBatch", value = "发布批次", defaultValue = "0") @RequestParam(value = "releaseBatch", defaultValue = "0") Long releaseBatch,
            @ApiParam(name = "imageUrl", value = "镜像地址", required = true) @RequestParam(value = "imageUrl") String imageUrl,
            @ApiParam(name = "yamlFilePath", value = "镜像地址", defaultValue = "") @RequestParam(value = "yamlFilePath", defaultValue = "") String yamlFilePath) throws Exception {
        this.kubeYamlService.CreateYamlData(appEnvId, cuser, nameSpace, service, deployment, containers, releaseStrategy, replicas
                , releaseBatch, imageUrl, yamlFilePath);
    }

    @ApiOperation(value = "更新yaml信息", notes = "更新yaml信息")
    @PutMapping(value = "/app/env/{appEnvId}/yaml")
    public void updateYamlInfoByAppEnvId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long cuser,
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(name = "deploymentStrategy", value = "部署方式", required = true) @RequestParam(value = "deploymentStrategy") String deploymentStrategy,
            @ApiParam(name = "nameSpace", value = "命名空间", defaultValue = "") @RequestParam(value = "nameSpace", defaultValue = "") String nameSpace,
            @ApiParam(name = "service", value = "服务", defaultValue = "") @RequestParam(value = "service", defaultValue = "") String service,
            @ApiParam(name = "deployment", value = "部署", defaultValue = "") @RequestParam(value = "deployment", defaultValue = "") String deployment,
            @ApiParam(name = "containers", value = "容器", defaultValue = "") @RequestParam(value = "containers", defaultValue = "") String containers,
            @ApiParam(name = "releaseStrategy", value = "发布策略", required = true) @RequestParam(value = "releaseStrategy") String releaseStrategy,
            @ApiParam(name = "replicas", value = "副本数量", defaultValue = "0") @RequestParam(value = "replicas", defaultValue = "0") Integer replicas,
            @ApiParam(name = "releaseBatch", value = "发布批次", defaultValue = "0") @RequestParam(value = "releaseBatch", defaultValue = "0") Long releaseBatch,
            @ApiParam(name = "imageUrl", value = "镜像地址", required = true) @RequestParam(value = "imageUrl") String imageUrl,
            @ApiParam(name = "yamlFilePath", value = "镜像地址", defaultValue = "") @RequestParam(value = "yamlFilePath", defaultValue = "") String yamlFilePath) throws Exception {
        this.kubeYamlService.updateYamlData(appEnvId, cuser, nameSpace, service, deployment, containers, releaseStrategy, replicas
                , releaseBatch, imageUrl, yamlFilePath);
    }

    @ApiOperation(value = "获取yaml信息", notes = "获取yaml信息")
    @GetMapping(value = "/app/env/{appEnvId}/yaml")
    public KubeYamlDataV1 getYamlInfoByAppEnvId(@ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId) {
        return BeanUtils.convertType(this.kubeYamlService.findYamlDataByEnvId(appEnvId), KubeYamlDataV1.class);
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
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId
    ) {
        try {
            return this.kubeYamlService.findNameSpaces(appEnvId);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "获取服务", notes = "获取服务")
    @GetMapping(value = "/app/env/{appEnvId}/cluster/allServices")
    public List<String> getServiceByNameSpace(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace
    ) {
        try {
            return this.kubeYamlService.getServiceByNameSpace(appEnvId, namespace);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "获取部署", notes = "获取部署")
    @GetMapping(value = "/app/env/{appEnvId}/cluster/allDeployment")
    public HashMap<String, Object> getDeploymentByNameSpaceAndService(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace,
            @ApiParam(value = "service", name = "service", required = true) @RequestParam(value = "service") String service
    ) {
        try {
            return this.kubeYamlService.getDeploymentByNameSpaceAndService(appEnvId, namespace, service);
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @ApiOperation(value = "创建服务", notes = "创建服务")
    @PostMapping(value = "/app/env/{appEnvId}/cluster/service")
    public void createServiceByNameSpace(
            @ApiParam(value = "appEnvId", name = "appEnvId", required = true) @PathVariable(value = "appEnvId") Long appEnvId,
            @ApiParam(value = "namespace", name = "namespace", required = true) @RequestParam(value = "namespace") String namespace,
            @ApiParam(value = "name", name = "name", required = true) @RequestParam(value = "name") String name,
            @ApiParam(value = "port", name = "port", required = true) @RequestParam(value = "port") Integer port
    ) {
        try {
            this.kubeYamlService.createServiceByNameSpace(appEnvId, namespace, name, port);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

}
