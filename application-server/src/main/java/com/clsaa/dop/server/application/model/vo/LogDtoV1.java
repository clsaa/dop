package com.clsaa.dop.server.application.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 操作日志传输层类
 *
 * @author joyren
 */
@Data
public class LogDtoV1 implements Serializable {
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
}