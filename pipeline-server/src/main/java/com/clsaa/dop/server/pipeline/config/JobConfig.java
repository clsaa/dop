package com.clsaa.dop.server.pipeline.config;

/**
 * 生成创建jenkins是多需要的xml
 *
 * @author 张富利
 * @since 2019-03-09
 */

public class JobConfig {
    private String script;
    private String xml;
    public JobConfig(String script, String timing) {
        this.script = script;
        this.xml = "<flow-definition plugin=\"workflow-job@2.31\">\n" +
                "  <actions>\n" +
                "\t\t<org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin=\"pipeline-model-definition@1.3.4\"/>\n" +
                "\t</actions>\n" +
                "  <description></description>\n" +
                "  <keepDependencies>false</keepDependencies>\n" +
                "  <properties>\n" +
                "\t\t<com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin=\"gitlab-plugin@1.5.12\">\n" +
                "\t\t\t<gitLabConnection/>\n" +
                "\t\t</com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>\n" +
                "\t\t<org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty plugin=\"gitlab-logo@1.0.3\">\n" +
                "\t\t\t<repositoryName/>\n" +
                "\t\t</org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty>\n" +
                "\t\t<com.sonyericsson.rebuild.RebuildSettings plugin=\"rebuild@1.29\">\n" +
                "\t\t\t<autoRebuild>false</autoRebuild>\n" +
                "\t\t\t<rebuildDisabled>false</rebuildDisabled>\n" +
                "\t\t</com.sonyericsson.rebuild.RebuildSettings>\n" +
                "\t\t<org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "\t\t\t<triggers>\n" +
                "\t\t\t\t<hudson.triggers.TimerTrigger>\n" +
                "\t\t\t\t\t<spec>"+ timing +"</spec>\n" +
                "\t\t\t\t</hudson.triggers.TimerTrigger>\n" +
                "\t\t\t</triggers>\n" +
                "\t\t</org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "\t</properties>\n" +
                "  <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.61.1\">\n" +
                "    <script>\n" +
                this.script +
                "</script>\n" +
                "    <sandbox>true</sandbox>\n" +
                "  </definition>\n" +
                "  <triggers/>\n" +
                "  <disabled>false</disabled>\n" +
                "</flow-definition>";
    }

    public JobConfig(String git, String path, String timing) {
        this.xml = "<?xml version='1.1' encoding='UTF-8'?>\n"
                +"<flow-definition plugin=\"workflow-job@2.31\">\n" +
                "\t<actions>\n" +
                "\t\t<org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin=\"pipeline-model-definition@1.3.4\"/>\n" +
                "\t</actions>\n" +
                "\t<description/>\n" +
                "\t<keepDependencies>false</keepDependencies>\n" +
                "\t<properties>\n" +
                "\t\t<org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "\t\t\t<triggers>\n" +
                "\t\t\t\t<hudson.triggers.TimerTrigger>\n" +
                "\t\t\t\t\t<spec>"+timing+"</spec>\n" +
                "\t\t\t\t</hudson.triggers.TimerTrigger>\n" +
                "\t\t\t</triggers>\n" +
                "\t\t</org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>\n" +
                "\t\t<com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin=\"gitlab-plugin@1.5.12\">\n" +
                "\t\t\t<gitLabConnection/>\n" +
                "\t\t</com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>\n" +
                "\t\t<org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty plugin=\"gitlab-logo@1.0.3\">\n" +
                "\t\t\t<repositoryName/>\n" +
                "\t\t</org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty>\n" +
                "\t\t<com.sonyericsson.rebuild.RebuildSettings plugin=\"rebuild@1.29\">\n" +
                "\t\t\t<autoRebuild>false</autoRebuild>\n" +
                "\t\t\t<rebuildDisabled>false</rebuildDisabled>\n" +
                "\t\t</com.sonyericsson.rebuild.RebuildSettings>\n" +
                "\t</properties>\n" +
                "\t<definition class=\"org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition\" plugin=\"workflow-cps@2.61\">\n" +
                "\t\t<scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.9.1\">\n" +
                "\t\t\t<configVersion>2</configVersion>\n" +
                "\t\t\t<userRemoteConfigs>\n" +
                "\t\t\t\t<hudson.plugins.git.UserRemoteConfig>\n" +
                "\t\t\t\t\t<url>" +
                git.trim()+"</url>\n" +
                "\t\t\t\t</hudson.plugins.git.UserRemoteConfig>\n" +
                "\t\t\t</userRemoteConfigs>\n" +
                "\t\t\t<branches>\n" +
                "\t\t\t\t<hudson.plugins.git.BranchSpec>\n" +
                "\t\t\t\t\t<name>*/master</name>\n" +
                "\t\t\t\t</hudson.plugins.git.BranchSpec>\n" +
                "\t\t\t</branches>\n" +
                "\t\t\t<doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n" +
                "\t\t\t<submoduleCfg class=\"list\"/>\n" +
                "\t\t\t<extensions/>\n" +
                "\t\t</scm>\n" +
                "\t\t<scriptPath>"+path.trim()+"</scriptPath>\n" +
                "\t\t<lightweight>true</lightweight>\n" +
                "\t</definition>\n" +
                "\t<triggers/>\n" +
                "\t<disabled>false</disabled>\n" +
                "</flow-definition>";
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
