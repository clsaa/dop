package com.clsaa.dop.server.permission.model.po;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户数据表，用于数据权限的控制
 *
 * @author lzy
 *
 * since :2019.3.19
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user_data",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"rule_id","user_id","field_value"})}) //引入@Table注解，name赋值为表名
//重写SQL删除语句
@SQLDelete(sql = "update t_user_data set is_deleted = true ,user_id = uuid_short() where id = ?")
@Where(clause = "is_deleted =false")
public class UserData implements Serializable {
    private static final long serialVersionUID = 552000265L;

    /**
     * 用户数据ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 规则ID
     */
    @Basic
    @Column(name = "rule_id")
    private Long ruleId;
    /**
     *  用户ID
     */
    @Basic
    @Column(name = "user_id")
    private Long userId;
    /**
     * 参数值
     */
    @Basic
    @Column(name = "field_value")
    private Long fieldValue;
    /**
     * 描述
     */
    @Basic
    @Column(name = "description")
    private String description;

    /* 表里都要有的字段*/
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
     * 创建者
     */
    @Basic
    @Column(name = "cuser")
    private Long cuser;
    /**
     * 修改者
     */
    @Basic
    @Column(name = "muser")
    private Long muser;
    /**
     * 删除标记
     */
    @Basic
    @Column(name="is_deleted")
    private boolean deleted;
    /* 表里都要有的字段*/
}
