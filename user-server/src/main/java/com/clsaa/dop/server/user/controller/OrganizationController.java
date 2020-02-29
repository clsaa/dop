package com.clsaa.dop.server.user.controller;

import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.config.HttpHeaders;
import com.clsaa.dop.server.user.model.bo.OrganizationBoV1;
import com.clsaa.dop.server.user.model.vo.OrganizationV1;
import com.clsaa.dop.server.user.service.OrganizationService;
import com.clsaa.dop.server.user.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织接口实现类
 *
 * @author joyren
 */
@RestController
@CrossOrigin
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @ApiOperation(value = "创建组织", notes = "创建一个组织，创建者即组织拥有者")
    @PostMapping("/v1/organizations")
    public void addOrganizationV1(@ApiParam("组织名称,长度大于等于4小于等于20") @RequestParam("name") String name,
                                  @ApiParam("组织描述,长度大于等于10小于等于180") @RequestParam("description") String description,
                                  @ApiParam("登录用户id") @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserId) {
        BizAssert.validParam(StringUtils.hasText(name) && name.length() >= 4 && name.length() <= 18,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织名称,长度应大于3小于19"));
        BizAssert.validParam(StringUtils.hasText(description)
                        && description.length() >= 4
                        && name.length() <= 180,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织描述,长度应大于3小于180"));
        BizAssert.validParam(loginUserId != null && loginUserId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户未登录"));
        this.organizationService.addOrganization(name, description, loginUserId, loginUserId, loginUserId);
    }

    @ApiOperation(value = "删除组织", notes = "删除组织")
    @DeleteMapping("/v1/organizations/{id}")
    public void deleteOrganizationById(@ApiParam("组织id") @PathVariable("id") Long id,
                                       @ApiParam("登录用户id") @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserId) {
        BizAssert.validParam(id != null && id != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织不存在"));
        BizAssert.validParam(loginUserId != null && loginUserId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户未登录"));
        this.organizationService.deleteOrganizationById(id, loginUserId);
    }

    @ApiOperation(value = "修改组织信息", notes = "修改组织信息")
    @PutMapping("/v1/organizations/{id}")
    public void updateOrganizationV1(@ApiParam("组织id") @PathVariable("id") Long id,
                                     @ApiParam("组织名称,长度大于等于4小于等于20") @RequestParam("name") String name,
                                     @ApiParam("组织描述,长度大于等于10小于等于180") @RequestParam("description") String description,
                                     @ApiParam("登录用户id") @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserId) {
        BizAssert.validParam(id != null && id != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织不存在"));
        BizAssert.validParam(StringUtils.hasText(name) && name.length() >= 4 && name.length() <= 18,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织名称,长度大于等于4小于等于20"));
        BizAssert.validParam(StringUtils.hasText(description)
                        && description.length() >= 4
                        && name.length() <= 180,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织描述,长度大于等于10小于等于180"));
        BizAssert.validParam(loginUserId != null && loginUserId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户未登录"));
        this.organizationService.updateOrganization(id, name, description, loginUserId);
    }

    @ApiOperation(value = "查询登录用户所属组织", notes = "查询登录用户所属组织")
    @GetMapping("/v1/organizations")
    public List<OrganizationV1> findOrganizationV1ByUser(@ApiParam("登录用户id") @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserId) {
        BizAssert.validParam(loginUserId != null && loginUserId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户未登录"));
        return this.organizationService.findOrganizationByUser(loginUserId)
                .stream()
                .map(o -> BeanUtils.convertType(o, OrganizationV1.class))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "查询组织详情", notes = "查询组织详情")
    @GetMapping("/v1/organizations/{id}")
    public OrganizationV1 findOrganizationV1ById(@ApiParam("组织id") @PathVariable("id") Long id,
                                                 @ApiParam("登录用户id") @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUserId) {
        BizAssert.validParam(id != null && id != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "组织不存在"));
        BizAssert.validParam(loginUserId != null && loginUserId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户未登录"));
        return BeanUtils.convertType(this.organizationService.findOrganizationById(id), OrganizationV1.class);
    }
}
