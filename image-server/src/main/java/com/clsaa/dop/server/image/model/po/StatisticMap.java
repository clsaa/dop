package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * StatisticMap对应的harbor类
 * @author xzt
 * @since 2019-4-5
 */
@Getter
@Setter
public class StatisticMap {
  @JsonProperty("private_project_count")
  private Integer privateProjectCount;

  @JsonProperty("private_repo_count")
  private Integer privateRepoCount;

  @JsonProperty("public_project_count")
  private Integer publicProjectCount;

  @JsonProperty("public_repo_count")
  private Integer publicRepoCount;

  @JsonProperty("total_project_count")
  private Integer totalProjectCount;

  @JsonProperty("total_repo_count")
  private Integer totalRepoCount;
}

