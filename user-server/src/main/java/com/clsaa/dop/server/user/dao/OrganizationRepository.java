package com.clsaa.dop.server.user.dao;


import com.clsaa.dop.server.user.model.po.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author joyren
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    /**
     * 通过id查询组织
     *
     * @param id 组织id
     * @return {@link Organization}
     */
    Organization findOrganizationById(Long id);

    /**
     * 通过id列表查询组织
     *
     * @param ids 组织id列表
     * @return {@link List<Organization>}
     */
    List<Organization> findOrganizationByIdIn(List<Long> ids);
}
