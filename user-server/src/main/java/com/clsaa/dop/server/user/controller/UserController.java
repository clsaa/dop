package com.clsaa.dop.server.user.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.clsaa.dop.server.user.config.BizCodes;
import com.clsaa.dop.server.user.config.HttpHeaders;
import com.clsaa.dop.server.user.model.dto.ResetDtoV1;
import com.clsaa.dop.server.user.model.po.UserCredential;
import com.clsaa.dop.server.user.model.vo.UserV1;
import com.clsaa.dop.server.user.util.BeanUtils;
import com.clsaa.dop.server.user.service.UserService;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户API接口实现类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "添加用户", notes = "用户必须先进行注册，然后通过接收到的激活码添加一个用户")
    @PostMapping("/v1/users")
    public void addUserV1(@ApiParam(value = "激活码") @RequestParam(value = "code") String code) {
        this.userService.addUser(code);
    }

    @ApiOperation(value = "更新用户密码", notes = "用户必须先获取修改密码所需验证码，通过验证码更新密码")
    @PutMapping("/v1/users/password")
    public void updateUserPassword(@RequestBody ResetDtoV1 resetDtoV1) {
        this.userService.updateUserPassword(resetDtoV1.getEmail(), resetDtoV1.getPassword(), resetDtoV1.getCode());
    }

    @ApiOperation(value = "根据id查询用户信息", notes = "根据id查询用户信息，若用户不存在返回null")
    @GetMapping("/v1/users/{id}")
    public UserV1 findUserByIdV1(@ApiParam(value = "用户id") @PathVariable("id") Long id) {
        return BeanUtils.convertType(this.userService.findUserById(id), UserV1.class);
    }

    @ApiOperation(value = "查询已登录用户信息", notes = "查询已登录用户信息，若用户不存在返回null")
    @GetMapping("/v1/users")
    public UserV1 findLoginUserByIdV1(@ApiParam(value = "用户id") @RequestHeader(HttpHeaders.X_LOGIN_USER) Long id) {
        return BeanUtils.convertType(this.userService.findUserById(id), UserV1.class);
    }

    @ApiOperation(value = "根据邮箱和密码查询用户信息", notes = "根据邮箱和密码查询用户信息，若密码错误或用户不存在返回null," +
            "密码经过RSA加密且进行BASE64URL编码，此接口一般给登陆服务使用")
    @GetMapping("/v1/users/byCredential")
    public UserV1 findUserByEmailAndPassword(@RequestParam("email") String email,
                                             @RequestParam("password") String password) {
        return BeanUtils.convertType(this.userService.findUserByEmailAndPassword(email, password), UserV1.class);
    }

    @ApiOperation(value = "根据邮箱或账户名模糊匹配用户", notes = "根据邮箱和账户名模糊匹配用户, 仅支持前缀匹配, ")
    @GetMapping("/v1/users/search")
    public Pagination<UserV1> searchUserByOrganizationIdAndEmailOrPassword(@ApiParam(value = "关键字，姓名或邮箱")
                                                                               @RequestParam(value = "key", required = false) String key,
                                                                           @ApiParam(value = "组织id，若为空则不限制，若填写则搜索对应组织下的用户")
                                                                           @RequestParam(value = "organizationId", required = false) Long organizationId,
                                                                           @ApiParam(value = "页号")
                                                                               @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                                                           @ApiParam(value = "页大小")
                                                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return this.userService.searchUserByEmailOrPassword(key, organizationId, pageNo, pageSize);
    }
}
