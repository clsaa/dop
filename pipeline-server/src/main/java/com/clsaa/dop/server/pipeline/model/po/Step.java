package com.clsaa.dop.server.pipeline.model.po;


import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 流水线阶段所做任务持久层对象
 * @author 张富利
 * @since 2019-03-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step {
    /**
     * 任务名称
     */
    @SerializedName("taskName")
    @Enumerated(EnumType.STRING)
    private TaskType taskName;
    public enum TaskType {
        PullCode,
        BuildMaven,
        BuildNode,
        BuildDjanggo,
        BuildDocker,
        PushDocker,
        CustomScript,
        Deploy
    }

    /**
     * git地址
     */
    @SerializedName("gitUrl")
    private String gitUrl;

    /**
     * docker用户名
     */
    @SerializedName("dockerUserName")
    private String dockerUserName;

    /**
     * docker密码
     */
    @SerializedName("dockerPassword")
    private String dockerPassword;

    /**
     * docker镜像版本
     */
    @SerializedName("repositoryVersion")
    private String repositoryVersion;

    /**
     * docker镜像名称
     */
    @SerializedName("repository")
    private String repository;

    /**
     * deploy
     */
    @SerializedName("deploy")
    private String deploy;

    /**
     * 预留k8s token
     */
    @SerializedName("token")
    private String token;

    /**
     * 预留k8s ip:port
     */
    @SerializedName("ip")
    private String ip;

    /**
     * shell
     */
    @SerializedName("shell")
    private String shell;


    public TaskType getTaskName() {
        return taskName;
    }

    public void setTaskName(TaskType taskName) {
        this.taskName = taskName;
    }
}
