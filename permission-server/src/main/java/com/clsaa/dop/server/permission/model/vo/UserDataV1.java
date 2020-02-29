package com.clsaa.dop.server.permission.model.vo;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户数据表VO层
 *
 * @author lzy
 *
 * since :2019.3.21
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataV1 implements Serializable {
    /**
     * 用户数据ID
     */
    private Long id;

    /**
     * 规则ID
     */
    private Long ruleId;
    /**
     *  用户ID
     */
    private Long userId;
    /**
     * 参数值
     */
    private Long fieldValue;
    /**
     * 描述
     */
    private String description;
    /* 表里都要有的字段*/
    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 创建者
     */

    private Long cuser;

    /* 表里都要有的字段*/
}
