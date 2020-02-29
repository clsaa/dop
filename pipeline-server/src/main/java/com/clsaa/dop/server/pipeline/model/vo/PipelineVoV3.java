package com.clsaa.dop.server.pipeline.model.vo;

import com.clsaa.dop.server.pipeline.model.po.Jenkinsfile;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;

/***
 * 展示全部流水线信息---for table
 * **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineVoV3 {
    /**
     * 流水线id
     */
    private String id;

    /**
     * 流水线名称
     */
    private String name;

    /**
     * 修改人
     */
    private String cuser;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;
}
