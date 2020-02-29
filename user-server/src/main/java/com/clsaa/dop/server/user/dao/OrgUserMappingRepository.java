package com.clsaa.dop.server.user.dao;


import com.clsaa.dop.server.user.model.po.OrgUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author joyren
 */
public interface OrgUserMappingRepository extends JpaRepository<OrgUserMapping, Long> {

    /**
     * 根据组织id和用户id查询关联关系
     *
     * @param organizationId 组织id
     * @param userId         用户id
     * @return {@link OrgUserMapping}
     */
    OrgUserMapping findOrgUserMappingByOrganizationIdAndUserId(Long organizationId, Long userId);

    /**
     * 根据组织id查询所有用户关联关系
     *
     * @param organizationId 组织id
     * @return {@link List<OrgUserMapping>}
     */
    List<OrgUserMapping> findOrgUserMappingsByOrganizationId(Long organizationId);

    /**
     * 根据用户id查询所有用户关联关系
     *
     * @param userId 用户id
     * @return {@link List<OrgUserMapping>}
     */
    List<OrgUserMapping> findOrgUserMappingsByUserId(Long userId);
}
