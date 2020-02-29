package com.clsaa.dop.server.image.model.bo;

import lombok.Getter;
import lombok.Setter;


/**
 *
 */

@Getter
@Setter
public class DetailedTagScanOverviewBO {
    private String digest;

    private String scanStatus;

    private Integer jobId;

    private Integer severity;

    private String detailsKey;

    private DetailedTagScanOverviewComponentsBO components;
}
