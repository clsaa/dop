package com.clsaa.dop.server.pipeline.model.bo;

import com.clsaa.dop.server.pipeline.model.po.Jenkinsfile;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * 流水线信息业务层对象
 * @author 张富利
 * @since 2019-03-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineBoV2 {
    /**
     * 流水线id
     */
    @SerializedName("id")
    private String id;

    /**
     * 流水线名称
     */
    @SerializedName("name")
    private String name;

    /**
     * 监听方式
     */
    @SerializedName("monitor")
    private int monitor;

    /**
     * 定时触发时间间隔
     */
    @Field("timing")
    @SerializedName("timing")
    private Long timing;
    /**
     *  配置方式
     * */
    @Field("config")
    @SerializedName("config")
    private int config;

    /**
     *  Jenkinsfile
     * */
    @SerializedName("jenkinsfile")
    private Jenkinsfile jenkinsfile;

    /**
     * 流水线阶段
     */
    @SerializedName("stages")
    private ArrayList<Stage> stages;

    /**
     * 修改人
     */
    @SerializedName("cuser")
    private Long cuser;

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

    public int getMonitor() {
        return monitor;
    }

    public void setMonitor(int monitor) {
        this.monitor = monitor;
    }

    public Long getTiming() {
        return timing;
    }

    public void setTiming(Long timing) {
        this.timing = timing;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
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

    public Long getCuser() {
        return cuser;
    }

    public void setCuser(Long cuser) {
        this.cuser = cuser;
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
}
