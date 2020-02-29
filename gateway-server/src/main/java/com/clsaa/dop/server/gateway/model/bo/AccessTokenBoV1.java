package com.clsaa.dop.server.gateway.model.bo;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenBoV1 implements Serializable {
    private static final long serialVersionUID = 4L;

    /**
     * id
     */
    private Long id;
    /**
     * 外键, 指向client的主键
     */
    private Long clientId;
    /**
     * access_token的值,36位UUID
     */
    private String token;
    /**
     * 过期时间
     */
    private Timestamp expires;
    /**
     * 创建时间
     */
    private Timestamp ctime;
    /**
     * 检查Token是否失效
     *
     * @return 是否失效 true为失效
     */
    public boolean isExpired() {
        return expires.getTime() < System.currentTimeMillis();
    }
}