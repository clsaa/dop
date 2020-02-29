package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * AccessLog 对于项目的操作信息
 * @author  xzt
 * @since 2019-3-25
 */
@Setter
@Getter
public class AccessLog {
  @JsonProperty("log_id")
  private Integer logId;

  @JsonProperty("username")
  private String username;

  @JsonProperty("repo_name")
  private String repoName;

  @JsonProperty("repo_tag")
  private String repoTag;

  @JsonProperty("operation")
  private String operation;

  @JsonProperty("op_time")
  private String opTime;
}

