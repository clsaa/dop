package com.clsaa.dop.server.user.model.bo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 组织-用户关系业务层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2019-04-08
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgUserMappingBoV1 implements Serializable {

    private static final long serialVersionUID = 6906097418517272171L;
    /**
     * 组织-用户关系id
     */
    @Id
    private Long id;
    /**
     * 组id
     */
    @Basic
    private Long organizationId;
    /**
     * 用户id
     */
    @Basic
    private Long userId;
}