package com.clsaa.dop.server.application.model.bo;

import com.clsaa.dop.server.application.model.po.AppEnv;
import lombok.*;


/**
 * 应用环境业务层对象
 *
 * @author Bowen
 * @since 2019-3-14
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppEnvBoV1 {

    private Long id;

    /**
     * 环境名称
     */
    private String title;


    /**
     * 部署策略
     */
    private AppEnv.DeploymentStrategy deploymentStrategy;


    /**
     * 环境级别
     */
    private AppEnv.EnvironmentLevel environmentLevel;

}
