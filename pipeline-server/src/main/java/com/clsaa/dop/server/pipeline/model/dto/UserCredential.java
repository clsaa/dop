package com.clsaa.dop.server.pipeline.model.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

public class UserCredential implements Serializable {
    public enum Type {
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

        Type(String code) {
        }
    }

    private static final long serialVersionUID = 6906097418517275871L;
    /**
     * 用户凭据id
     */

    private Long id;
    /**
     * 所属用户id
     */

    private Long userId;
    /**
     * 标识（手机号、邮箱、用户名或第三方应用的唯一标识）
     */

    private String identifier;
    /**
     * 凭据(密码或token)
     */

    private String credential;
    /**
     * 创建时间
     */

    private LocalDateTime ctime;
    /**
     * 修改时间
     */

    private LocalDateTime mtime;
    /**
     * 用户认证类型
     */
    @Enumerated(EnumType.STRING)
    private Type type;
}
