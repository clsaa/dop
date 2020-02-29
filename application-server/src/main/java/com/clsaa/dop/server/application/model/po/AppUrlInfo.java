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
 * 应用变量实体类
 *
 * @author ZhengBowen
 * @version v1
 * @summary 应用变量实体类
 * @since 2019-3-7
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update app_basic_url set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class AppUrlInfo {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 应用ID
     */
    @Column(nullable = false, name = "app_id")
    private Long appId;

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
     * 仓库URL
     */
    @Column(nullable = false, name = "warehouse_url")
    private String warehouseUrl;


    /**
     * 镜像地址
     */
    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    /**
     * 开发数据库URL
     */
    @Column(nullable = true, name = "production_db_url")
    private String productionDbUrl;

    /**
     * 测试数据库URL
     */
    @Column(nullable = true, name = "test_db_url")
    private String testDbUrl;

    /**
     * 开发域名
     */
    @Column(nullable = true, name = "production_domain")
    private String productionDomain;

    /**
     * 测试域名
     */
    @Column(nullable = true, name = "test_domain")
    private String testDomain;
    //@OneToOne(cascade = CascadeType.ALL,mappedBy = "AppUrlInfo")
    //private App app;

}
