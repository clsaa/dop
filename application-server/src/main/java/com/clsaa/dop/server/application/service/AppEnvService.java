package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.PermissionConfig;
import com.clsaa.dop.server.application.dao.AppEnvRepository;
import com.clsaa.dop.server.application.model.bo.AppEnvBoV1;
import com.clsaa.dop.server.application.model.po.AppEnv;
import com.clsaa.dop.server.application.model.vo.PipelineIdAndNameV1;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "AppEnvService")
public class AppEnvService {


    @Autowired
    AppEnvRepository appEnvRepository;
    @Autowired
    PipelineService pipelineService;
    @Autowired
    private PermissionConfig permissionConfig;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    KubeCredentialService kubeCredentialService;

    /**
     * 根据appId查询环境信息
     *
     * @param appID appID
     * @return{@link List<AppEnvBoV1> }
     */
    public List<AppEnvBoV1> findEnvironmentByAppId(Long loginUser, Long appID) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewAppEnv(), loginUser)
                , BizCodes.NO_PERMISSION);
    
        return this.appEnvRepository.findAllByAppId(appID).stream().map(l -> BeanUtils.convertType(l, AppEnvBoV1.class)).collect(Collectors.toList());
    }

    //public void findPipelineByAppEnvId(Long appEnvId) {
    //
    //}



    /**
     * 创建环境信息
     *
     * @param appEnv appEnv
     * @return
     */
    public void createAppEnv(AppEnv appEnv) {
        this.appEnvRepository.saveAndFlush(appEnv);
        if (AppEnv.DeploymentStrategy.KUBERNETES == appEnv.getDeploymentStrategy()) {
            this.kubeCredentialService.createCredentialByAppEnvId(appEnv.getCuser(), appEnv.getId());
        }
    }


    /**
     * 根据ID查询环境详情信息
     *
     * @param id
     * @return AppEnvBoV1
     */
    public AppEnvBoV1 findEnvironmentDetailById(Long loginUser, Long id) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewAppEnvDetail(), loginUser)
                , BizCodes.NO_PERMISSION);
        AppEnv appEnv = this.appEnvRepository.findById(id).orElse(null);
        return BeanUtils.convertType(appEnv, AppEnvBoV1.class);
    }

    /**
     * 根据ID查询AppId
     *
     * @param id
     * @return Long
     */
    public Long findAppIdById(Long id) {
        AppEnv appEnv = this.appEnvRepository.findById(id).orElse(null);
        return appEnv.getAppId();
    }


    /**
     * 创建环境
     *
     * @param appId              appId
     * @param loginUser              创建者
     * @param title              名称
     * @param environmentLever   环境级别
     * @param deploymentStrategy 发布策略
     */
    public void createEnvironmentByAppId(Long appId, Long loginUser, String title, String environmentLever, String deploymentStrategy) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getCreateAppEnv(), loginUser)
                , BizCodes.NO_PERMISSION);
        AppEnv appEnv = AppEnv.builder()
                .appId(appId)
                .title(title)
                .cuser(loginUser)
                .muser(loginUser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .environmentLevel(AppEnv.EnvironmentLevel.valueOf(environmentLever))
                .deploymentStrategy(AppEnv.DeploymentStrategy.valueOf(deploymentStrategy))
                .build();
        this.appEnvRepository.saveAndFlush(appEnv);
        if (AppEnv.DeploymentStrategy.KUBERNETES == AppEnv.DeploymentStrategy.of(deploymentStrategy)) {
            this.kubeCredentialService.createCredentialByAppEnvId(loginUser, appEnv.getId());
        }

    }


    public PipelineIdAndNameV1 findPipelineByAppEnvId(Long loginUser, Long appEnvId) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewPipeline(), loginUser)
                , BizCodes.NO_PERMISSION);
        return this.pipelineService.findPipelineByAppEnvId(appEnvId);
    }

    /**
     * 根据ID删除环境信息
     *
     * @param id appId
     */
    public void deleteEnvironmentById(Long loginUser, Long id) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getDeleteAppEnv(), loginUser)
                , BizCodes.NO_PERMISSION);
        this.appEnvRepository.deleteById(id);
    }


}
