package com.clsaa.dop.server.image.model.bo;


import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *     项目日志的传输类
 * </p>
 * @author xzt
 * @since 2019-3-27
 */
@Setter
@Getter
public class AccessLogBO {
    private Integer logId;
    private String username;
    private String repoName;
    private String repoTag;
    private String operation;
    private String opTime;
}
