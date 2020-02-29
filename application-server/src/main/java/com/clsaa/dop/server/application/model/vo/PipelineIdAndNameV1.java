package com.clsaa.dop.server.application.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 流水线Id视图层对象
 * </p>
 *
 * @author 郑博文
 * @since 2019-4-13
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelineIdAndNameV1 {
    private Long id;
    private String name;
}
