package com.clsaa.dop.server.message.enums;

/**
 * 电子邮件状态常量
 *
 * @author joyren
 */
public enum EmailStatus {

    /**
     * 发送中
     */
    SENDING("SENDING"),
    /**
     * 已发送
     */
    SENT("SENT"),
    /**
     * 已取消
     */
    CANCELED("CANCELED");
    private String status;

    EmailStatus(String status) {
        this.status = status;
    }
}
