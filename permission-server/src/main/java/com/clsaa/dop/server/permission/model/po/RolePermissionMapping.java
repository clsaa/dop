package com.clsaa.dop.server.permission.model.po;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色关联功能点持久层对象，对应角色与功能点关联表中每条数据
 *
 * @author lzy
 *
 * since :2019.3.7
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//重写SQL删除语句
@SQLDelete(sql = "update t_role_permission_mapping set is_deleted = true,role_id = uuid_short() where id = ?")
@Where(clause = "is_deleted =false")
@Table(name = "t_role_permission_mapping",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"permission_id","role_id"})}) //引入@Table注解，name赋值为表名
public class RolePermissionMapping implements Serializable {

    private static final long serialVersionUID = 552000262L;
    /**
     * 角色-功能点关联ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 功能点id
     */
    @Basic
    @Column(name = "permission_id")
    private Long permissionId;
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
