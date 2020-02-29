package com.clsaa.dop.server.application.model.vo;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppV1 {
    /**
     * 应用id
     */
    private Long id;

    /**
     * 应用拥有者
     */
    private Long ouser;

    /**
     * 应用拥有者
     */
    private String ouserName;

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

}
