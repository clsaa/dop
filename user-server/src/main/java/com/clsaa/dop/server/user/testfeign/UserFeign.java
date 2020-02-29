package com.clsaa.dop.server.user.testfeign;

import com.clsaa.dop.server.user.config.FeignConfig;
import com.clsaa.dop.server.user.model.po.UserCredential;
import com.clsaa.dop.server.user.model.vo.UserCredentialV1;
import com.clsaa.dop.server.user.model.vo.UserV1;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author joyren
 */
@Component
@FeignClient(value = "user-server", configuration = FeignConfig.class)
public interface UserFeign {
    @ApiOperation(value = "根据邮箱或账户名模糊匹配用户", notes = "根据邮箱和账户名模糊匹配用户, 仅支持前缀匹配, ")
    @GetMapping(value = "/v1/users/search")
    Pagination<UserV1> searchUserByOrganizationIdAndEmailOrPassword(@ApiParam(value = "关键字，姓名或邮箱")
                                                                    @RequestParam(value = "key", required = false) String key,
                                                                    @ApiParam(value = "组织id，若为空则不限制，若填写则搜索对应组织下的用户")
                                                                    @RequestParam(value = "organizationId", required = false) Long organizationId,
                                                                    @ApiParam(value = "页号")
                                                                    @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                                                    @ApiParam(value = "页大小")
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);
    @GetMapping(value = "/v1/users/{userId}/credential")
    UserCredentialV1 getUserCredentialV1ByUserId(@PathVariable("userId") Long userId,
                                                 @RequestParam("type") UserCredential.Type type);
}