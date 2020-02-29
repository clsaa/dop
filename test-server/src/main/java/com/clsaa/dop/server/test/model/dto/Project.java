package com.clsaa.dop.server.test.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 09/05/2019
 */
@Data
public class Project {

    /**
     * 项目Id
     */
    private Long id;


    /**
     * 项目名称
     */
    private String title;

    /**
     * 创建人
     */
    private Long cuser;

    /**
     * 创建人
     */
    private String cuserName;

    /**
     * 创建日期
     */
    private LocalDateTime ctime;

    /**
     * 项目描述
     */
    private String description;
}
