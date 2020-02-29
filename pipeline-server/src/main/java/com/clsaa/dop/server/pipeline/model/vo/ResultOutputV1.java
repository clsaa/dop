package com.clsaa.dop.server.pipeline.model.vo;

import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResultOutputV1 {

    private ObjectId id;

    /**
     * 流水线id
     */

    private String pipelineId;

    /**
     * 运行结果
     */

    private String result;

    /**
     * 运行状态
     */
    private ResultOutput.Status status;

    /**
     * 创建时间
     */

    private LocalDateTime ctime;


    /**
     * 是否删除
     */

    private Boolean isDeleted;


}
