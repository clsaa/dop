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
 * @summary 应用变量实体类
 * @since 2019-3-12
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update app_variable set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class AppVariable {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 创建人
     */
    @Column(nullable = false, name = "cuser")
    private Long cuser;

    /**
     * 修改人
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
     * 应用ID
     */
    @Column(nullable = false, name = "app_id")
    private Long appId;

    /**
     * 键
     */
    @Column(nullable = false, name = "var_key")
    private String varKey;


    /**
     * 值
     */
    @Column(nullable = false, name = "var_value")
    private String varValue;



}
