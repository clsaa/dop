package com.clsaa.dop.server.user.controller;

import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.config.HttpHeaders;
import com.clsaa.dop.server.user.service.OrgUserMappingService;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 组织用户关联，接口实现类
 *
 * @author joyren
 */
@RestController
@CrossOrigin
public class OrgUserMappingController {
    @Autowired
    private OrgUserMappingService orgUserMappingService;

    @ApiOperation(value = "将用户添加到组织", notes = "将用户添加到组织")
    @PostMapping("/v1/orgUserMapping")
    public void addUserToOrganization(@ApiParam("组织id") @RequestParam("organizationId") Long organizationId,
                                      @ApiParam("要添加的用户id") @RequestParam("userId") Long userId,
                                      @ApiParam("登录用户id") @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserId) {
        BizAssert.validParam(organizationId != null && organizationId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织不存在"));
        BizAssert.validParam(userId != null && userId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织不存在"));
        BizAssert.validParam(loginUserId != null && loginUserId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户未登录"));
        this.orgUserMappingService.addOrgUserMapping(organizationId, userId, loginUserId);
    }

    @ApiOperation(value = "将用户从组织中删除", notes = "将用户从组织中删除")
    @DeleteMapping("/v1/orgUserMapping")
    public void deleteUserFromOrganization(@ApiParam("组织id") @RequestParam("organizationId") Long organizationId,
                                           @ApiParam("要添加的用户id") @RequestParam("userId") Long userId,
                                           @ApiParam("登录用户id") @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserId) {
        BizAssert.validParam(organizationId != null && organizationId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织不存在"));
        BizAssert.validParam(userId != null && userId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织不存在"));
        BizAssert.validParam(loginUserId != null && loginUserId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户未登录"));
        this.orgUserMappingService.deleteOrgUserMapping(organizationId, userId, loginUserId);
    }
}
