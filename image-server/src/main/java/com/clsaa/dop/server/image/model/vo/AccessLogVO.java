package com.clsaa.dop.server.image.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *  项目日志的视图层类
 * </p>
 * @author xzt
 * @since 2019-3-27
 */
@Getter
@Setter
public class AccessLogVO {
    private Integer logId;
    private String username;
    private String repoName;
    private String repoTag;
    private String operation;
    private String opTime;
}
