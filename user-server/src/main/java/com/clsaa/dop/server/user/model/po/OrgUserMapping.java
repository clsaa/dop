package com.clsaa.dop.server.user.model.po;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 组织-用户关系持久层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2019-02-20
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_org_user_mapping", schema = "db_dop_user_server",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "organization_id"})},
        indexes = {@Index(columnList = "user_id, organization_id")})
public class OrgUserMapping implements Serializable {

    private static final long serialVersionUID = 6906097418517275871L;
    /**
     * 组织-用户关系id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 组id
     */
    @Basic
    @Column(name = "organization_id")
    private Long organizationId;
    /**
     * 用户id
     */
    @Basic
    @Column(name = "user_id")
    private Long userId;
}