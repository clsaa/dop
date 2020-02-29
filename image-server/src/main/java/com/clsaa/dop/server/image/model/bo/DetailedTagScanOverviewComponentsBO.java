package com.clsaa.dop.server.image.model.bo;

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
public class DetailedTagScanOverviewComponentsBO {
  private Integer total;
  private List<ComponentOverviewEntryBO> summary;

}

