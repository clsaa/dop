package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * RetagReq
 */

@Setter
@Getter
public class RetagReq {
  @JsonProperty("tag")
  private String tag;

  @JsonProperty("src_image")
  private String srcImage;

  @JsonProperty("override")
  private Boolean override;
}

