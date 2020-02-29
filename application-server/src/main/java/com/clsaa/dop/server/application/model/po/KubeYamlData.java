package com.clsaa.dop.server.application.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 应用Yaml信息实体类
 *
 * @author ZhengBowen
 * @version v1
 * @summary 应用Yaml信息实体类
 * @since 2019-3-18
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update kube_yaml_data set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class KubeYamlData {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 创建者
     */
    @Column(nullable = false, name = "cuser")
    private Long cuser;

    /**
     * 修改者
     */
    @Column(nullable = false, name = "muser")
    private Long muser;

    /**
     * 创建时间
     */
    @Column(nullable = false, name = "ctime")
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    @Column(nullable = false, name = "mtime")
    private LocalDateTime mtime;

    /**
     * 是否删除
     */
    @Column(nullable = false, name = "is_deleted")
    private boolean is_deleted;


    /**
     * APPENVID
     */
    @Column(nullable = false, name = "app_env_id")
    private Long appEnvId;

    /**
     * 命名空间
     */
    @Column(name = "name_space")
    private String nameSpace;

    /**
     * 对应服务
     */
    @Column(name = "service")
    private String service;

    /**
     * 对应部署
     */
    @Column(name = "deployment")
    private String deployment;

    /**
     * 对应容器
     */
    @Column(name = "containers")
    private String containers;

    /**
     * 发布批次
     */
    @Column(name = "release_batch")
    private Long releaseBatch;

    /**
     * 发布策略
     */
    @Column(name = "release_strategy")
    @Enumerated(EnumType.STRING)
    private KubeYamlData.ReleaseStrategy releaseStrategy;


    /**
     * yaml文件相对路径
     */
    @Column(name = "yaml_file_path")
    private String yamlFilePath;

    /**
     * 副本数量
     */
    @Column(name = "replicas")
    private Integer replicas;

    /**
     * 部署的yaml
     */
    @Column(name = "deployment_editable_yaml", length = 1000)
    private String deploymentEditableYaml;

    public enum ReleaseStrategy {

        /**
         * 蓝绿发布
         */
        BLUE_GREEN("BLUE_GREEN"),

        /**
         * 滚动升级
         */
        ROLLING_UPDATE("ROLLING_UPDATE"),

        /**
         * 分批发布
         */
        BATCH("BATCH"),
        ;

        private String code;

        ReleaseStrategy(String code) {
            this.code = code;
        }
    }
}
