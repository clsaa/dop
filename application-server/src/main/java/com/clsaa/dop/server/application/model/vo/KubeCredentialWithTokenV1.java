package com.clsaa.dop.server.application.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KubeCredentialWithTokenV1 {
    /**
     * 目标集群Url
     */

    private String targetClusterUrl;

    private String targetClusterToken;
}
