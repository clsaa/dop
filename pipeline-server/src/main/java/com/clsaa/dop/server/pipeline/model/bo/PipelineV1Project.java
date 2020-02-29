package com.clsaa.dop.server.pipeline.model.bo;

import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 向应用管理模块返回数据
 * */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineV1Project {

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
}
