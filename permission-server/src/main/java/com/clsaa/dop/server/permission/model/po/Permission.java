package com.clsaa.dop.server.permission.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 功能点持久层对象，对应功能点表中每条数据
 *
 * @author lzy
 *
 *
 * since :2019.3.1
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_permission",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}) //引入@Table注解，name赋值为表名
//重写SQL删除语句
@SQLDelete(sql = "update t_permission set is_deleted = true, name = CONCAT(uuid(),name) where id = ?")
@Where(clause = "is_deleted =false")
public class Permission implements Serializable {

    private static final long serialVersionUID = 552000264L;
    /**
     * 功能点ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    /**
     * 父功能点ID
     */
    @Basic
    @Column(name="parent_id")
    private Long parentId;
    /**
     * 功能点名称
     */
    @Basic
    @Column(name="name")
    private String name;
    /**
     * 是否私有
     */
    @Basic
    @Column(name="is_private")
    private Integer isPrivate;
    /**
     * 功能点描述
     */

    @Basic
    @Column(name="description")
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
