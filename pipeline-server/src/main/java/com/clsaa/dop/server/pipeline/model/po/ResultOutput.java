package com.clsaa.dop.server.pipeline.model.po;


import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dop_pipeline_result")
public class ResultOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("id")
    private ObjectId id;

    /**
     * 流水线id
     */
    @Field("pipelineId")
    @SerializedName("pipelineId")
    private String pipelineId;

    /**
     * 运行结果
     */
    @Field("result")
    @SerializedName("result")
    private String result;

    /**
     * 运行状态
     */
    @Field("status")
    @SerializedName("status")
    @Enumerated(EnumType.STRING)
    private Status status;
    public enum Status {
        RUNNING("RUNNING"),
        FINISHED("FINISHED");
        private String code;
        Status(String code) {
        }
    }

    /**
     * 创建时间
     */
    @Field("ctime")
    @SerializedName("ctime")
    private LocalDateTime ctime;


    /**
     * 是否删除
     */
    @Field("isDeleted")
    @SerializedName("isDeleted")
    private Boolean isDeleted;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCtime() {
        return ctime;
    }

    public void setCtime(LocalDateTime ctime) {
        this.ctime = ctime;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
