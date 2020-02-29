package com.clsaa.dop.server.pipeline.model.po;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * 流水线信息持久层对象
 * @author 张富利
 * @since 2019-03-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dop_pipeline_server")
public class Pipeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("id")
    private String id;

    /**
     * 流水线名称
     */
    @Field("name")
    @SerializedName("name")
    private String name;

    /**
     * 监听方式
     */
    @Field("monitor")
    @SerializedName("monitor")
    @Enumerated(EnumType.STRING)
    private Monitor monitor;


    public enum Monitor {AutomaticTrigger, ManualTrigger, TimingTrigger;}

    /**
     * 定时触发时间间隔
     */
    @Field("timing")
    @SerializedName("timing")
    private String timing;

    /**
     *  配置方式
     * */
    @Field("config")
    @SerializedName("config")
    private Config config;
    public enum Config {HasJenkinsfile, NoJenkinsfile;}

    /**
     *  Jenkinsfile
     * */
    @Field("jenkinsfile")
    @SerializedName("jenkinsfile")
    private Jenkinsfile jenkinsfile;

    /**
     * 流水线阶段
     */
    @Field("stages")
    @SerializedName("stages")
    private ArrayList<Stage> stages;

    /**
     * 流水线所属项目的id
     */
    @Field("appId")
    @SerializedName("appId")
    private Long appId;

    /**
     * 流水线所属环境的id
     */
    @Field("appEnvId")
    @SerializedName("appEnvId")
    private Long appEnvId;

    /**
     * 创建时间
     */
    @Field("ctime")
    @SerializedName("ctime")
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    @Field("mtime")
    @SerializedName("mtime")
    private LocalDateTime mtime;

    /**
     * 修改人
     */
    @Field("cuser")
    @SerializedName("cuser")
    private Long cuser;

    /**
     * 是否删除
     */
    @Field("isDeleted")
    @SerializedName("isDeleted")
    private Boolean isDeleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Jenkinsfile getJenkinsfile() {
        return jenkinsfile;
    }

    public void setJenkinsfile(Jenkinsfile jenkinsfile) {
        this.jenkinsfile = jenkinsfile;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void setStages(ArrayList<Stage> stages) {
        this.stages = stages;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getAppEnvId() {
        return appEnvId;
    }

    public void setAppEnvId(Long appEnvId) {
        this.appEnvId = appEnvId;
    }

    public LocalDateTime getCtime() {
        return ctime;
    }

    public void setCtime(LocalDateTime ctime) {
        this.ctime = ctime;
    }

    public LocalDateTime getMtime() {
        return mtime;
    }

    public void setMtime(LocalDateTime mtime) {
        this.mtime = mtime;
    }

    public Long getCuser() {
        return cuser;
    }

    public void setCuser(Long cuser) {
        this.cuser = cuser;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
