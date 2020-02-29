package com.clsaa.dop.server.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "permission")
public class PermissionConfig {
    private String createProject;
    private String viewProject;
    private String createApp;
    private String viewApp;
    private String editApp;
    private String deleteApp;
    private String viewAppUrl;
    private String editAppUrl;
    private String createAppEnv;
    private String viewAppEnv;
    private String viewAppEnvDetail;
    private String viewPipeline;
    private String deleteAppEnv;
    private String createVar;
    private String viewVar;
    private String editVar;
    private String deleteVar;
    private String viewCluster;
    private String editCluster;
    private String createYamlData;
    private String editYamlData;
    private String viewYamlData;
    private String editDeploymentYaml;
    private String viewNameSpace;
    private String viewService;
    private String createService;
    private String viewDeployment;
    private Long projectManagerAndProjectRuleId;
    private String projectRuleFieldName;
    private Long projectManagerAndAppRuleId;
    private String appRuleFieldName;
    private Long developerAndAppRuleId;
    private Long developerAndProjectRuleId;
    private String deleteMemberFromProject;
    private String addMemberToProject;
}
