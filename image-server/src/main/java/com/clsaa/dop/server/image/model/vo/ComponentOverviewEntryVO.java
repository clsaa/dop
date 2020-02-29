package com.clsaa.dop.server.image.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * ComponentOverviewEntry对应的视图层类
 * @author xzt
 * @since 2019-3-30
 */

@Setter
@Getter
public class ComponentOverviewEntryVO {
  private Integer severity;
  private Integer count;
}