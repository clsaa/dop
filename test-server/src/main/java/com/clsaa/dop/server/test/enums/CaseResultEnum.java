package com.clsaa.dop.server.test.enums;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
public enum CaseResultEnum {

    SUCCESS("成功"),
    FAIL("失败"),
    TOCHECK("待核查"),
    NONUSE("不可用"),
    BLOCKED("阻塞")
    ;

    private String comment;

    CaseResultEnum(String comment) {
        this.comment = comment;
    }

}
