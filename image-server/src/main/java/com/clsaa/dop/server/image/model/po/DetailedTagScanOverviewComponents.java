package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The components overview of the image.
 */

@Getter
@Setter
public class DetailedTagScanOverviewComponents {
  @JsonProperty("total")
  private Integer total;

  @JsonProperty("summary")
  private List<ComponentOverviewEntry> summary;

}

