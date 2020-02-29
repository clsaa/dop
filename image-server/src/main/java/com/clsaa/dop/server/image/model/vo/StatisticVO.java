package com.clsaa.dop.server.image.model.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * 视图层的数据统计类
 * @author xzt
 * @since 2019-4-5
 */
@Getter
@Setter

public class StatisticVO {
    /**
     * 私有项目数量
     */
    private Integer privateProjectCount;
    /**
     * 私有仓库数量
     */
    private Integer privateRepoCount;
    /**
     * 公开项目数量
     */
    private Integer publicProjectCount;
    /**
     * 公开镜像数量
     */
    private Integer publicRepoCount;
    /**
     * 全部项目数量
     */
    private Integer totalProjectCount;
    /**
     * 全部仓库数量
     */
    private Integer totalRepoCount;
}
