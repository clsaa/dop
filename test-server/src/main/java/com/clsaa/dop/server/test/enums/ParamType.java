package com.clsaa.dop.server.test.enums;

public enum  ParamType {
    STRING("String"),
    INTEGER("Integer"),
    LONG("Long"),
    DOUBLE("double"),
    BOOLEAN("Boolean"),
    BIGDECIMAL("BigDecimal")
    ;

    private String comment;

    private ParamType(String comment) {
        this.comment = comment;
    }
}
