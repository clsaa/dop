package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * ProjectMember的harbor对应实体类
 * @author  xzt
 * @since 2019-3-25
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember   {
  @JsonProperty("role_id")
  private Integer roleId;

  @JsonProperty("member_user")
  private UserEntity memberUser;

  @JsonProperty("member_group")
  private UserGroup memberGroup;

}

