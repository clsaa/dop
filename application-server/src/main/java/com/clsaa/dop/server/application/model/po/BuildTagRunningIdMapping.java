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

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update build_tag_running_id_mapping set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class BuildTagRunningIdMapping {
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
     * 版本号
     */
    @Column(nullable = false, name = "build_tag")
    private String buildTag;

    /**
     * 运行id
     */
    @Column(nullable = false, name = "running_id")
    private String runningId;

}
