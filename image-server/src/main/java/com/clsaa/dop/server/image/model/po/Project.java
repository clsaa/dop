package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * json构建的项目对象
 * @author xzt
 * @since  2019-3-23
 */
@Getter
@Setter
public class Project   {
  /**
   * 项目的id
   */
  @JsonProperty("project_id")
  private Integer projectId;
  /**
   * 项目创建者的id
   */
  @JsonProperty("owner_id")
  private Integer ownerId;
  /**
   * 项目的名称
   */
  @JsonProperty("name")
  private String name;
  /**
   * 项目创建时间
   */
  @JsonProperty("creation_time")
  private String creationTime;
  /**
   * 项目最后修改时间
   */
  @JsonProperty("update_time")
  private String updateTime;
  /**
   * 项目的状态，是否被删除
   */
  @JsonProperty("deleted")
  private Boolean deleted;
  /**
   * 项目创建者的名字
   */
  @JsonProperty("owner_name")
  private String ownerName;
  /**
   * 用来控制项目的public属性是否可以被修改
   */
  @JsonProperty("togglable")
  private Boolean togglable;
  /**
   *  当前用户的用户类型
   */
  @JsonProperty("current_user_role_id")
  private Integer currentUserRoleId;
  /**
   *  镜像仓库数
   */
  @JsonProperty("repo_count")
  private Integer repoCount;
  /**
   * chart数量
   */
  @JsonProperty("chart_count")
  private Integer chartCount;
  /**
   * 项目基本设置信息
   */
  @JsonProperty("metadata")
  private ProjectMetadata metadata;
}

