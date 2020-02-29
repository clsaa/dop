package com.clsaa.dop.server.login.enums;

/**
 * 客户端类型常量
 *
 * @author joyren
 */

public enum Client {
    /**
     * DOPWeb
     */
    DOP_WEB("DOP_WEB");
    private String status;

    Client(String status) {
        this.status = status;
    }
}