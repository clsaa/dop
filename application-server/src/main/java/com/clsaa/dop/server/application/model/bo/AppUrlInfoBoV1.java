package com.clsaa.dop.server.application.model.bo;

import lombok.*;

import java.time.LocalDateTime;


/**
 * 应用Url信息业务层对象
 *
 * @author Bowen
 * @since 2019-3-10
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUrlInfoBoV1 {


    private Long id;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 创建者
     */
    private Long cuser;

    /**
     * 修改者
     */
    private Long muser;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    private LocalDateTime mtime;

    /**
     * 仓库URL
     */
    private String warehouseUrl;

    /**
     * 镜像地址
     */
    private String imageUrl;
    /**
     * 开发数据库URL
     */
    private String productionDbUrl;

    /**
     * 测试数据库URL
     */
    private String testDbUrl;

    /**
     * 开发域名
     */
    private String productionDomain;

    /**
     * 测试域名
     */
    private String testDomain;
}
