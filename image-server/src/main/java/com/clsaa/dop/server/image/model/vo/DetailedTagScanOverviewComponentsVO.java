package com.clsaa.dop.server.image.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The components overview of the image.
 * @author xzt
 * @since 2019-4-6
 */

@Getter
@Setter
public class DetailedTagScanOverviewComponentsVO {
  private Integer total;
  private List<ComponentOverviewEntryVO> summary;

}

