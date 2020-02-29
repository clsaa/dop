package com.clsaa.dop.server.login.model.po;

import com.clsaa.dop.server.login.enums.Client;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 登入日志持久层对象
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
@Table(name = "t_login_log", schema = "db_dop_user_login")
public class LoginLog implements Serializable {
    private static final long serialVersionUID = 6906097418517275871L;

    public enum Status {
        /**
         * 成功
         */
        SUCCESS("SUCCESS"),
        /**
         * 失败
         */
        FAIL("FAIL");
        private String status;

        Status(String status) {
            this.status = status;
        }
    }

    /**
     * 登陆记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * userId
     */
    @Basic
    @Column(name = "user_id")
    private Long userId;
    /**
     * 用户请求时的ip地址
     */
    @Basic
    @Column(name = "login_ip")
    private String loginIp;
    /**
     * 设备ID(PC为uuid)
     */
    @Basic
    @Column(name = "device_id")
    private String deviceId;
    /**
     * 来源
     */
    @Basic
    @Column(name = "client")
    @Enumerated(EnumType.STRING)
    private Client client;
    /**
     * 创建时间
     */
    @Basic
    @Column(name = "ctime")
    private LocalDateTime ctime;
    /**
     * 登陆状态
     */
    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}