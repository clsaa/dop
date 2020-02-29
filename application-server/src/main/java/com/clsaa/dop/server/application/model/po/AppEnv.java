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
 * 应用环境信息实体类
 *
 * @author ZhengBowen
 * @version v1
 * @summary 应用环境信息实体类
 * @since 2019-3-14
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update app_env set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class AppEnv {

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
     * APPID
     */
    @Column(nullable = false, name = "app_id")
    private Long appId;

    /**
     * 部署策略
     */
    @Column(nullable = false, name = "deployment_strategy")
    @Enumerated(EnumType.STRING)
    private AppEnv.DeploymentStrategy deploymentStrategy;

    /**
     * 环境名称
     */
    @Column(nullable = false, name = "title")
    private String title;




    /**
     * 环境级别
     */
    @Column(nullable = false, name = "environment_level")
    @Enumerated(EnumType.STRING)
    private AppEnv.EnvironmentLevel environmentLevel;


    public enum EnvironmentLevel {

        /**
         * 日常环境
         */
        DAILY("DAILY"),

        /**
         * 预发环境
         */
        PRERELEASE("PRERELEASE"),

        /**
         * 正式环境
         */
        RELEASE("RELEASE"),
        ;

        private String code;

        EnvironmentLevel(String code) {
            this.code = code;
        }
    }


    public enum DeploymentStrategy {
        /**
         * Kubernetes部署
         */
        KUBERNETES("KUBERNETES"),
        ;

        private String code;

        public static DeploymentStrategy of(String code) {
            for (DeploymentStrategy e : DeploymentStrategy.values()) {
                if (e.code.equals(code)) {
                    return e;
                }
            }
            return null;
        }


        DeploymentStrategy(String code) {
            this.code = code;
        }
    }
}
