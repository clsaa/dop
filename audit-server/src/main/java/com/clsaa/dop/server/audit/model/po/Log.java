package com.clsaa.dop.server.audit.model.po;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 操作日志持久层类
 *
 * @author joyren
 */
@Data
@Builder
public class Log implements Serializable {
    @Id
    @Field("id")
    private String id;
    /**
     * 服务id，用于唯一区分日志所属服务，与Eureka中注册的服务id保持一致
     */
    @Field("serviceId")
    private String serviceId;
    /**
     * 功能id，用于唯一区分日志所属功能
     */
    @Field("functionId")
    private String functionId;
    /**
     * 用户id，用于唯一区分操作人员
     */
    @Field("userId")
    private String userId;
    /**
     * 值(JSON)，业务方根据需求自定义
     */
    @Field("values")
    private Map<String, Object> values;
    /**
     * 业务发生时间，单位：毫秒
     */
    @Field("otime")
    private Long otime;
    /**
     * 创建时间
     */
    @Field("ctime")
    private LocalDateTime ctime;
}
