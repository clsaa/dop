package com.clsaa.dop.server.gateway.model.po;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * AccessToken持久层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-29
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_access_token", schema = "db_dop_gateway")
public class AccessToken implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 外键, 指向client的主键
     */
    @Basic
    @Column(name = "client_id")
    private Long clientId;
    /**
     * access_token的值,36位UUID
     */
    @Basic
    @Column(name = "token")
    private String token;
    /**
     * 过期时间
     */
    @Basic
    @Column(name = "expires")
    private Timestamp expires;
    /**
     * 创建时间
     */
    @Basic
    @Column(name = "ctime")
    private Timestamp ctime;

}