package com.clsaa.dop.server.permission.model.po;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户关联角色持久层对象，对应用户与角色关联表中每条数据
 *
 * @author lzy
 *
 * since :2019.3.9
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//重写SQL删除语句
@SQLDelete(sql = "update t_user_role_mapping set is_deleted = true ,user_id = uuid_short() where id = ?")
@Where(clause = "is_deleted =false")
@Table(name = "t_user_role_mapping",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","role_id"})}) //引入@Table注解，name赋值为表名
public class UserRoleMapping implements Serializable {
    private static final long serialVersionUID = 552000261L;
    /**
     * 用户-角色关联ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户id
     */
    @Basic
    @Column(name = "user_id")
    private Long userId;
    /**
     * 角色id
     */
    @Basic
    @Column(name = "role_id")
    private Long roleId;

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
