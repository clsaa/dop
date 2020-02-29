package com.clsaa.dop.server.application.model.bo;

import com.clsaa.dop.server.application.model.po.KubeYamlData;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KubeYamlDataBoV1 {

    private Long id;

    /**
     * 命名空间
     */
    private String nameSpace;

    /**
     * 对应服务
     */
    private String service;

    /**
     * 对应部署
     */
    private String deployment;

    /**
     * 对应容器
     */
    private String containers;


    /**
     * 发布策略
     */
    private KubeYamlData.ReleaseStrategy releaseStrategy;


    /**
     * 发布批次
     */

    private Long releaseBatch;


    /**
     * yaml文件相对路径
     */
    private String yamlFilePath;

    /**
     * 副本数量
     */
    private Integer replicas;

    /**
     * 部署的yaml
     */
    private String deploymentEditableYaml;


}


