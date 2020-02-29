package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * ProjectReq 新建项目时所需内容
 * @author xzt
 * @since 2019-3-26
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectReq {
  @JsonProperty("project_name")
  private String projectName;

  @JsonProperty("metadata")
  private ProjectMetadata metadata;

}

