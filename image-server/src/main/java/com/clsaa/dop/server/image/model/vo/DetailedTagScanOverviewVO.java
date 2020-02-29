package com.clsaa.dop.server.image.model.vo;

import lombok.Getter;
import lombok.Setter;


/**
 *
 */

@Getter
@Setter
public class DetailedTagScanOverviewVO {
    private String digest;

    private String scanStatus;

    private Integer jobId;

    private Integer severity;

    private String detailsKey;

    private DetailedTagScanOverviewComponentsVO components;
}
