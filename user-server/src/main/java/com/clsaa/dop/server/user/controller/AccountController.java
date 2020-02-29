package com.clsaa.dop.server.user.controller;

import com.clsaa.dop.server.user.model.dto.RegisterDtoV1;
import com.clsaa.dop.server.user.model.dto.ResetDtoV1;
import com.clsaa.dop.server.user.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注册接口实现类
 *
 * @author joyren
 */
@CrossOrigin
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "获取账户登录、注册、修改密码时的RSA公钥", notes = "获取账户登录、注册、修改密码时的RSA公钥，密码传输时需用此公钥加密")
    @GetMapping("/v1/account/RSAPublicKey")
    public String getAccountRSAPublicKey() {
        return this.accountService.getAccountRSAPublicKey();
    }

    @ApiOperation(value = "注册", notes = "注册，注册后会向用户邮箱发送激活邮件。密码传输时应使用RSA公钥加密并进行Base64URL编码后传输。" +
            "姓名：至少一个数字、字母、下划线，不能以下划线结尾" +
            "密码：长度至少为6个字符，最大长度为20, 必须包含一个数字 0-9、包含一个小写字符、包含一个大写字符、包含一个的特殊字符@#$%.-=")
    @PostMapping("/v1/account/register")
    public void registerAccount(@RequestBody RegisterDtoV1 registerDtoV1) {
        this.accountService.register(registerDtoV1.getName(), registerDtoV1.getEmail(), registerDtoV1.getPassword());
    }

    @ApiOperation(value = "修改密码", notes = "修改密码，申请修改密码后会向用户邮箱发送验证邮件。密码传输时应使用RSA公钥加密并进行Base64URL编码后传输。" +
            "密码：长度至少为6个字符，最大长度为20, 必须包含一个数字 0-9、包含一个小写字符、包含一个大写字符、包含一个的特殊字符@#$%.-=")
    @PostMapping("/v1/account/reset")
    public void resetAccountPassword(@RequestParam("email") String email) {
        this.accountService.resetAccountPassword(email);
    }
}
