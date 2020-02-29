package com.clsaa.dop.server.test.enums;

public enum OperationType {

    REQUEST("请求"),
    WAIT("等待一段时间"),
    IFELSE("IF ELSE 判断"),
    LOOP("循环"),
    ;

    private String comment;

    private OperationType(String comment) {
        this.comment = comment;
    }
}
