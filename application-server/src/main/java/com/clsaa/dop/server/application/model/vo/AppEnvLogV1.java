package com.clsaa.dop.server.application.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppEnvLogV1 {


    /**
     * 运行id
     */
    private String id;

    /**
     * 代码仓库地址
     */
    private String commitUrl;

    /**
     * 镜像仓库地址
     */
    private String imageUrl;


    /**
     * 操作员
     */
    private String ruserName;

    /**
     * 运行时间
     */
    private LocalDateTime rtime;

    /**
     * 运行结果
     */
    private String status;

    /**
     * 环境日志
     */
    private String appEnvLog;
}
