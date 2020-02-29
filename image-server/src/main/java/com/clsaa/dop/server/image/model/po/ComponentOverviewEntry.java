package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * ComponentOverviewEntry对应的harbor类
 * @author xzt
 * @since 2019-3-30
 */

@Setter
@Getter
public class ComponentOverviewEntry {
  @JsonProperty("severity")
  private Integer severity;

  @JsonProperty("count")
  private Integer count;
}

