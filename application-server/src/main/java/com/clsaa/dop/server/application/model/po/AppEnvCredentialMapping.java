package com.clsaa.dop.server.application.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update env_credential_mapping set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
@Table(name = "t_app_env_cluster_mapping", schema = "db_dop_application_server",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"app_env_id", "credential_id"})},
        indexes = {@Index(columnList = "app_env_id, credential_id")})
public class AppEnvCredentialMapping {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * APPENVID
     */
    @Column(nullable = false, name = "app_env_id")
    private Long appEnvId;

    /**
     * CredentialID
     */
    @Column(nullable = false, name = "credential_id")
    private Long credentialId;

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


}
