package com.clsaa.dop.server.user.model.po;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织持久层对象，用户组织是组织架构中的顶层结构
 *
 * @author joyren
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_organization", schema = "db_dop_user")
@SQLDelete(sql = "update t_organization set is_deleted = true where id = ?")
@Where(clause = "is_deleted = false")
public class Organization implements Serializable {
    private static final long serialVersionUID = 6906097411517275871L;
    /**
     * 组织id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 组织名
     */
    @Basic
    @Column(name = "name")
    private String name;
    /**
     * 组织描述
     */
    @Basic
    @Column(name = "description")
    private String description;
    /**
     * 组织编码
     */
    @Basic
    @Column(name = "code")
    private String code;
    /**
     * 创建人
     */
    @Basic
    @Column(name = "cuser")
    private Long cuser;
    /**
     * 修改人
     */
    @Basic
    @Column(name = "muser")
    private Long muser;
    /**
     * 所属人(组织拥有者)
     */
    @Basic
    @Column(name = "ouser")
    private Long ouser;
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
     * 是否删除
     */
    @Basic
    @Column(name = "is_deleted")
    private Boolean deleted;
}
