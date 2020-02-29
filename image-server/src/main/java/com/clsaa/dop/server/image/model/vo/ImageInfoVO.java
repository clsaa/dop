package com.clsaa.dop.server.image.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 镜像信息的视图层对应类
 * @author xzt
 * @since 2019-4-6
 */
@Getter
@Setter

public class ImageInfoVO {
    private String digest;

    private String name;

    private String size;

    private String architecture;

    private String os;

    private String dockerVersion;

    private String author;

    private String created;

    private Object signature;

    private DetailedTagScanOverviewVO scanOverview;

    private List<LabelVO> labels;
}
