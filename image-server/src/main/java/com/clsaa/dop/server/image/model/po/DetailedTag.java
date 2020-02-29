package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * DetailedTag对应的harbor类
 * @author xzt
 * @since 2019-3-30
 */

@Getter
@Setter
public class DetailedTag {
  @JsonProperty("digest")
  private String digest;

  @JsonProperty("name")
  private String name;

  @JsonProperty("size")
  private Integer size;

  @JsonProperty("architecture")
  private String architecture;

  @JsonProperty("os")
  private String os;

  @JsonProperty("docker_version")
  private String dockerVersion;

  @JsonProperty("author")
  private String author;

  @JsonProperty("created")
  private String created;

  @JsonProperty("signature")
  private Object signature;

  @JsonProperty("scan_overview")
  private DetailedTagScanOverview scanOverview;

  @JsonProperty("labels")
  private List<Label> labels;
  


}

