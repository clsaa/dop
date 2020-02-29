package com.clsaa.dop.server.test.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 09/05/2019
 */
@Data
public class Application {

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
