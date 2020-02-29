package com.clsaa.dop.server.user.controller;

import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.model.po.UserCredential;
import com.clsaa.dop.server.user.model.vo.UserCredentialV1;
import com.clsaa.dop.server.user.service.UserCredentialService;
import com.clsaa.dop.server.user.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户凭证接口
 *
 * @author joyren
 * @since 2019-03-25
 */
@RestController
@CrossOrigin
public class UserCredentialController {
    @Autowired
    private UserCredentialService userCredentialService;

    @ApiOperation(value = "添加用户凭证", notes = "添加一个用户凭证，比如GitlabToken，JenkinsToken，Harbor用户名密码等")
    @PostMapping("/v1/users/{userId}/credential")
    public void addUserCredential(@ApiParam(value = "用户id") @PathVariable("userId") Long userId,
                                  @ApiParam(value = "凭证标识，如邮箱、用户名等") @RequestParam("identifier") String identifier,
                                  @ApiParam(value = "凭证，如token，密码等") @RequestParam("credential") String credential,
                                  @ApiParam(value = "凭证类型，用于标识凭证的用途") @RequestParam("type") UserCredential.Type type) {
        BizAssert.validParam(userId != null && userId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户id不存在"));
        BizAssert.validParam(StringUtils.hasText(identifier),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "凭证标识为空"));
        BizAssert.validParam(StringUtils.hasText(credential),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "凭证为空"));
        BizAssert.validParam(type != UserCredential.Type.DOP_LOGIN_EMAIL, BizCodes.INVALID_OPERATION_FOR_CREDENTIAL);
        this.userCredentialService.addUserCredential(userId, identifier, credential, type);
    }

    @ApiOperation(value = "删除用户凭证", notes = "根据用户id和凭证类型删除一个用户凭证")
    @DeleteMapping("/v1/users/{userId}/credential")
    void deleteUserCredential(@ApiParam(value = "用户id") @PathVariable("userId") Long userId,
                              @ApiParam(value = "凭证类型，用于标识凭证的用途") @RequestParam("type") UserCredential.Type type) {
        BizAssert.validParam(userId != null && userId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户id不存在"));
        BizAssert.validParam(type != UserCredential.Type.DOP_LOGIN_EMAIL, BizCodes.INVALID_OPERATION_FOR_CREDENTIAL);
        this.userCredentialService.deleteUserCredentialByUserIdAndType(userId, type);
    }

    @ApiOperation(value = "更新用户凭证", notes = "根据用户id和凭证类型更新一个用户凭证")
    @PutMapping("/v1/users/{userId}/credential")
    public void updateUserCredential(@ApiParam(value = "用户id") @PathVariable("userId") Long userId,
                                     @ApiParam(value = "凭证，如token，密码等") @RequestParam("credential") String credential,
                                     @ApiParam(value = "凭证类型，用于标识凭证的用途") @RequestParam("type") UserCredential.Type type) {
        BizAssert.validParam(userId != null && userId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户id不存在"));
        BizAssert.validParam(StringUtils.hasText(credential),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "凭证为空"));
        BizAssert.validParam(type != UserCredential.Type.DOP_LOGIN_EMAIL, BizCodes.INVALID_OPERATION_FOR_CREDENTIAL);
        this.userCredentialService.updateUserCredentialByUserIdAndType(userId, credential, type);
    }

    @ApiOperation(value = "查询用户凭证", notes = "根据用户id查询用户凭证")
    @GetMapping("/v1/users/{userId}/credential")
    public UserCredentialV1 getUserCredentialV1ByUserId(@ApiParam(value = "用户id") @PathVariable("userId") Long userId,
                                                        @ApiParam(value = "凭证类型，用于标识凭证的用途") @RequestParam("type") UserCredential.Type type) {
        BizAssert.validParam(userId != null && userId != 0,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户id不存在"));
        BizAssert.validParam(type != UserCredential.Type.DOP_LOGIN_EMAIL, BizCodes.INVALID_OPERATION_FOR_CREDENTIAL);
        return BeanUtils.convertType(
                this.userCredentialService.findUserCredentialByUserIdAndType(userId, type), UserCredentialV1.class);
    }
}
