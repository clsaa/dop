package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * The overview of the scan result.  This is an optional property.
 */

@Getter
@Setter
public class DetailedTagScanOverview {
  @JsonProperty("digest")
  private String digest;

  @JsonProperty("scan_status")
  private String scanStatus;

  @JsonProperty("job_id")
  private Integer jobId;

  @JsonProperty("severity")
  private Integer severity;

  @JsonProperty("details_key")
  private String detailsKey;

  @JsonProperty("components")
  private DetailedTagScanOverviewComponents components;

 
}

