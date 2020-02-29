package com.clsaa.dop.server.audit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 操作日志视图层类
 *
 * @author joyren
 */
@Data
public class LogV1 implements Serializable {
    private String id;
    /**
     * 服务id，用于唯一区分日志所属服务，与Eureka中注册的服务id保持一致
     */
    private String serviceId;
    /**
     * 功能id，用于唯一区分日志所属功能
     */
    private String functionId;
    /**
     * 用户id，用于唯一区分操作人员
     */
    private String userId;
    /**
     * 值(JSON)，业务方根据需求自定义
     */
    private Map<String, Object> values;
    /**
     * 业务发生时间，单位：毫秒
     */
    private Long otime;
    /**
     * 创建时间
     */
    private LocalDateTime ctime;
}
