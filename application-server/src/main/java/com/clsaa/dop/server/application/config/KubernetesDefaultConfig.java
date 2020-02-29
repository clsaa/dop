package com.clsaa.dop.server.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "k8s")
public class KubernetesDefaultConfig {
    private String deploymentApiVersion;
    private String deploymentKind;
    private String deploymentLabelPrefix;
    private String deploymentContainerPortProtocol;
    private String deploymentImagePullPolicy;
    private String deploymentLocalTimeMountPath;
    private String deploymentLocalTimeMountName;
    private String deploymentTimeZoneMountPath;
    private String deploymentTimeZoneMountName;
    private String deploymentDnsPolicy;
    private String serviceApiVersion;
    private String serviceKind;
    private String ingressApiVersion;
    private String ingressKind;
    private String virtualServiceVersionLabel;
    private String serviceTypeNodeport;
    private String serviceNodeportHttpName;

}
