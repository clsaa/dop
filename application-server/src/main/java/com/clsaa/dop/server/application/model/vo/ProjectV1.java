package com.clsaa.dop.server.application.model.vo;

import com.clsaa.dop.server.application.model.po.Project;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectV1 {
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
     * 项目状态
     */
    private Project.Status status;

    /**
     * 项目描述
     */
    private String description;
}

