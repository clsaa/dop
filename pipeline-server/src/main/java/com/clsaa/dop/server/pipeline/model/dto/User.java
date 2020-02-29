package com.clsaa.dop.server.pipeline.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户持久层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user", schema = "db_dop_user_server",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}),
                @UniqueConstraint(columnNames = {"name"})},
        indexes = {@Index(columnList = "email"),
                @Index(columnList = "name")})
public class User implements Serializable {
    /**
     * 用户类型枚举
     *
     * @author 任贵杰
     */
    public enum Status {

        /**
         * 正常的
         */
        NORMAL("NORMAL"),
        /**
         * 禁止的
         */
        FORBIDDEN("FORBIDDEN"),
        ;

        private String code;

        Status(String code) {
        }
    }

    private static final long serialVersionUID = 6906097418517275871L;
    /**
     * 用户id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 用户姓名
     */
    @Basic
    @Column(name = "name")
    private String name;
    /**
     * 用户email地址
     */
    @Basic
    @Column(name = "email")
    private String email;
    /**
     * 用户头像URL
     */
    @Basic
    @Column(name = "avatar_url")
    private String avatarURL;
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
     * 用户状态
     */
    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
