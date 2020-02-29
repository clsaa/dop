package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * RoleRequest
 * @author xzt
 * @since 2019-3-25
 */
@Getter
@Setter
public class RoleRequest {
  /**
   * The role id 1 for projectAdmin, 2 for developer, 3 for guest, 4 for master
   **/
  @JsonProperty("role_id")
  private Integer roleId;
}

