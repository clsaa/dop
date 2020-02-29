package com.clsaa.dop.server.image.model.enumtype;

/**
 * 用户凭据类型枚举类
 */
public enum UserCredentialType {
    /**
     * DOP EMAIL类型登录凭据
     */
    DOP_LOGIN_EMAIL("DOP_LOGIN_EMAIL"),
    /**
     * DOP HARBOR镜像仓库的 EMAIL类型登录凭据
     */
    DOP_INNER_HARBOR_LOGIN_EMAIL("DOP_INNER_HARBOR_LOGIN_EMAIL"),
    /**
     * DOP 内部GITLAB Token
     */
    DOP_INNER_GITLAB_TOKEN("DOP_INNER_GITLAB_TOKEN");
    private String code;

    UserCredentialType(String code) {
    }
}
