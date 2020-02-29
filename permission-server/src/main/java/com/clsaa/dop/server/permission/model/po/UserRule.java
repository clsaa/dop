package com.clsaa.dop.server.permission.model.po;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户规则表，用于数据权限的控制
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
@Table(name = "t_user_rule",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id","field_name","rule"})}) //引入@Table注解，name赋值为表名
//重写SQL删除语句
@SQLDelete(sql = "update t_user_rule set is_deleted = true,field_name = CONCAT(uuid(),field_name) where id = ?")
@Where(clause = "is_deleted =false")
public class UserRule implements Serializable {
    private static final long serialVersionUID = 552000266L;

    /**
     * 用户数据规则ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 角色id
     */
    @Basic
    @Column(name = "role_id")
    private Long roleId;
    /**
     * 权限作用域参数名
     */
    @Basic
    @Column(name = "field_name")
    private String fieldName;
    /**
     * 规则
     */
    @Basic
    @Column(name = "rule")
    private String rule;
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
