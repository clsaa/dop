package com.clsaa.dop.server.application.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class AppBasicInfoV1 {

    /**
     * 应用拥有者
     */
    private Long ouser;

    /**
     * 应用名称
     */
    private String title;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 应用描述
     */
    private String description;
    /**
     * 镜像地址
     */
    private String imageUrl;

    /**
     * 仓库地址
     */
    private String warehouseUrl;


    /**
     * 开发数据库地址
     */
    private String productionDbUrl;

    /**
     * 测试数据库地址
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
