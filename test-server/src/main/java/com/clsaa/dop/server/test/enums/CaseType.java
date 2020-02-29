package com.clsaa.dop.server.test.enums;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
public enum  CaseType {

    MANUAL("手工"), INTERFACE("接口");

    private String comment;

    CaseType(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return this.comment;
    }
}
