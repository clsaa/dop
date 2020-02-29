package com.clsaa.dop.server.application.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUrlInfoV1 {


    /**
     * 仓库地址
     */
    private String warehouseUrl;
    /**
     * 镜像地址
     */
    private String imageUrl;

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
