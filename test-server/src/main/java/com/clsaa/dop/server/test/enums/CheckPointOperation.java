package com.clsaa.dop.server.test.enums;

public enum CheckPointOperation {
    EQUALS("等于", "="),
    NOTEQUALS("不等于", "!=")
    ;

    private String comment;
    private String expression;

    private CheckPointOperation(String comment, String expression) {
        this.comment = comment;
        this.expression = expression;
    }
}
