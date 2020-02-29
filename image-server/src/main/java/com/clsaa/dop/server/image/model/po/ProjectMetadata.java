package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ProjectMetadata 项目的状态信息
 * 对于注释，直接使用harbor的英文注释
 * @author xzt
 * @since 2019-3-24
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMetadata   {
  /**
   * The public status of the project. The valid values are "true", "false".
   */
  @JsonProperty("public")
  private String publicStatus;
  /**
   * Whether content trust is enabled or not.
   * If it is enabled, user cann't pull unsigned images from this project.
   * The valid values are "true", "false".
   */
  @JsonProperty("enable_content_trust")
  private String enableContentTrust;
  /**
   * Whether prevent the vulnerable images from running. The valid values are "true", "false".
   */
  @JsonProperty("prevent_vul")
  private String preventVul;
  /**
   * If the vulnerability is high than severity defined here, the images cann't be pulled.
   * The valid values are "negligible", "low", "medium", "high", "critical".
   */
  @JsonProperty("severity")
  private String severity;
  /**
   *   Whether scan images automatically when pushing. The valid values are "true", "false".
   */
  @JsonProperty("auto_scan")
  private String autoScan;
}

