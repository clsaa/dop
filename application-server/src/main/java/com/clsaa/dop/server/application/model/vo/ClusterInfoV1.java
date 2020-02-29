package com.clsaa.dop.server.application.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClusterInfoV1 {
    /**
     * 目标集群Url
     */

    private String targetClusterUrl;

    /**
     * 目标集群Token
     */

    private String targetClusterToken;
}
