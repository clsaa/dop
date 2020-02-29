package com.clsaa.dop.server.gateway.enums;

/**
 * 客户端类型枚举
 *
 * @author 任贵杰
 */
public enum ClientType {

    /**
     * web
     */
    WEB("WEB"),
    /**
     * app
     */
    APP("APP"),
    ;

    private String type;

    private ClientType(String type) {
    }
}