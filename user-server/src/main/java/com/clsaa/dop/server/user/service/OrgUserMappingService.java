package com.clsaa.dop.server.user.service;

import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.dao.OrgUserMappingRepository;
import com.clsaa.dop.server.user.model.bo.OrgUserMappingBoV1;
import com.clsaa.dop.server.user.model.bo.OrganizationBoV1;
import com.clsaa.dop.server.user.model.po.OrgUserMapping;
import com.clsaa.dop.server.user.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织用户关联，服务实现类
 *
 * @author joyren
 */
@Service
public class OrgUserMappingService {
    @Autowired
    private OrgUserMappingRepository orgUserMappingRepository;
    @Autowired
    private OrganizationService organizationService;

    public void addOrgUserMapping(Long organizationId, Long userId, Long operateUserId) {
        OrganizationBoV1 existOrganization = this.organizationService.findOrganizationById(organizationId);
        BizAssert.found(existOrganization != null, new BizCode(BizCodes.ERROR_DELETE.getCode(),
                "组织不存在"));
        BizAssert.pass(existOrganization.getOuser().equals(operateUserId), "只能由组织拥有者可以添加人员");
        OrgUserMapping orgUserMapping = OrgUserMapping.builder()
                .organizationId(organizationId)
                .userId(userId)
                .build();
        this.orgUserMappingRepository.saveAndFlush(orgUserMapping);
    }

    public void deleteOrgUserMapping(Long organizationId, Long userId, Long operateUserId) {
        OrgUserMapping existOrgUserMapping = this.orgUserMappingRepository
                .findOrgUserMappingByOrganizationIdAndUserId(organizationId, userId);
        BizAssert.found(existOrgUserMapping != null, new BizCode(BizCodes.ERROR_DELETE.getCode(),
                "用户未在此组织中"));
        OrganizationBoV1 existOrganization = this.organizationService.findOrganizationById(organizationId);
        BizAssert.found(existOrganization != null, new BizCode(BizCodes.ERROR_DELETE.getCode(),
                "组织不存在"));
        BizAssert.pass(existOrganization.getOuser().equals(organizationId) ||
                userId.equals(operateUserId), "只能由组织拥有者或用户自己删除");
        this.orgUserMappingRepository.delete(existOrgUserMapping);
    }

    public List<OrgUserMappingBoV1> findOrgUserMappingByOrganizationId(Long organizationId) {
        return this.orgUserMappingRepository.findOrgUserMappingsByOrganizationId(organizationId)
                .stream()
                .map(m -> BeanUtils.convertType(m, OrgUserMappingBoV1.class))
                .collect(Collectors.toList());
    }

    public List<OrgUserMappingBoV1> findOrgUserMappingByUserId(Long userId) {
        return this.orgUserMappingRepository.findOrgUserMappingsByUserId(userId)
                .stream()
                .map(m -> BeanUtils.convertType(m, OrgUserMappingBoV1.class))
                .collect(Collectors.toList());
    }
}
