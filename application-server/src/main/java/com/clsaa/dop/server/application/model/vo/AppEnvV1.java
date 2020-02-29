package com.clsaa.dop.server.application.model.vo;

import com.clsaa.dop.server.application.model.po.AppEnv;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * 应用环境视图层对象
 *
 * @author Bowen
 * @since 2019-3-14
 **/
@Getter
@Setter
public class AppEnvV1 {

    private Long id;

    /**
     * 环境名称
     */
    private String title;


    /**
     * 部署策略
     */
    private AppEnv.DeploymentStrategy deploymentStrategy;

}
