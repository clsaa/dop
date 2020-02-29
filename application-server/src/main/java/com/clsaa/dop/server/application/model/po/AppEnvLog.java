package com.clsaa.dop.server.application.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 应用环境日志实体类
 *
 * @author ZhengBowen
 * @version v1
 * @summary 应用环境日志实体类
 * @since 2019-4-16
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update app_env_log set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class AppEnvLog {
    /**
     * ID
     */
    @Id
    private String id;
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
     * 操作者
     */
    @Column(nullable = false, name = "ruser")
    private Long ruser;

    /**
     * 操作时间
     */
    @Column(nullable = false, name = "rtime")
    private LocalDateTime rtime;

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
     * 代码仓库地址
     */
    @Column(nullable = false, name = "commit_url")
    private String commitUrl;

    /**
     * 运行结果
     */
    @Column(nullable = false, name = "status")
    private String status;

    /**
     * 镜像仓库地址
     */
    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    /**
     * 环境日志
     */
    @Column(nullable = false, name = "app_env_log")
    private String appEnvLog;
}
