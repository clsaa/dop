package com.clsaa.dop.server.pipeline.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogInfoV1 {

    /**
     * 运行id
     */
    private String runningId;

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
    private Long ruser;

    /**
     * 运行时间
     */
    private LocalDateTime rtime;

    /**
     * 运行结果
     */
    private String Status;


}
