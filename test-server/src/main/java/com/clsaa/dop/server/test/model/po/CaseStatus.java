package com.clsaa.dop.server.test.model.po;

/**
 * @author xihao
 * @version 1.0
 * @since 05/03/2019
 */
public enum CaseStatus {
    NEW("新创建"),
    DESIGNING("设计中"),
    TESTING("测试中"),
    COMPLETE("已完成")
    ;

    private String comment;

    CaseStatus(String comment) {
        this.comment = comment;
    }
}
