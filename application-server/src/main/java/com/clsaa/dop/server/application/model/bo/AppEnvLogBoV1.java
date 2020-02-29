package com.clsaa.dop.server.application.model.bo;

import java.time.LocalDateTime;

public class AppEnvLogBoV1 {
    /**
     * ID
     */

    private String id;
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
     * 环境日志
     */
    private String appEnvLog;
}
