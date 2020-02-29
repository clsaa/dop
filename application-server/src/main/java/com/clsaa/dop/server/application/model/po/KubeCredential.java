package com.clsaa.dop.server.application.model.po;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 应用环境信息实体类
 *
 * @author ZhengBowen
 * @version v1
 * @summary Kubernetes 认证的url和token
 * @since 2019-3-23
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update kube_credential set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class KubeCredential {
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
     * 目标集群Url
     */
    @Column(name = "target_cluster_url")
    private String targetClusterUrl;

    /**
     * 目标集群Token
     */
    @Column(name = "target_cluster_token", length = 1000)
    private String targetClusterToken;

}
