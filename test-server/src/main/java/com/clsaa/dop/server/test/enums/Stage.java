package com.clsaa.dop.server.test.enums;

public enum Stage {
    PREPARE("准备"),
    TEST("测试"),
    DESTROY("销毁")
    ;

    private String comment;

    private Stage(String comment) {
        this.comment = comment;
    }
}
