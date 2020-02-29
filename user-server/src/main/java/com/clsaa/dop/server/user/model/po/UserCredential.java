package com.clsaa.dop.server.user.model.po;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户凭据持久层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2019-02-20
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user_credential", schema = "db_dop_user_server",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "type"}),
                @UniqueConstraint(columnNames = {"identifier", "type"})},
        indexes = {@Index(columnList = "userId")})
public class UserCredential implements Serializable {
    /**
     * 用户凭据类型枚举
     *
     * @author 任贵杰
     */
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 所属用户id
     */
    @Basic
    @Column(name = "userId")
    private Long userId;
    /**
     * 标识（手机号、邮箱、用户名或第三方应用的唯一标识）
     */
    @Basic
    @Column(name = "identifier")
    private String identifier;
    /**
     * 凭据(密码或token)
     */
    @Basic
    @Column(name = "credential")
    private String credential;
    /**
     * 创建时间
     */
    @Basic
    @Column(name = "ctime")
    private LocalDateTime ctime;
    /**
     * 修改时间
     */
    @Basic
    @Column(name = "mtime")
    private LocalDateTime mtime;
    /**
     * 用户认证类型
     */
    @Basic
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;
}