package com.clsaa.dop.server.login.controller;

import com.clsaa.dop.server.login.config.HttpHeaders;
import com.clsaa.dop.server.login.enums.Client;
import com.clsaa.dop.server.login.model.dto.LoginDtoV1;
import com.clsaa.dop.server.login.model.po.LoginLog;
import com.clsaa.dop.server.login.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author joyren
 */
@CrossOrigin
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "登录", notes = "通过邮箱-密码登录，方法返回UserLoginToken（JWT Token），" +
            "客户端调用后端需要登录的接口时在请求头X-Login-Token中携带此Token")
    @PostMapping("/v1/login")
    public String login(@RequestBody LoginDtoV1 loginDtoV1) {
        return this.loginService.login(loginDtoV1.getEmail(), loginDtoV1.getPassword(),
                loginDtoV1.getLoginIp(), loginDtoV1.getDeviceId(), loginDtoV1.getClient());
    }

    @ApiOperation(value = "校验X-Login-Token", notes = "校验X-Login-Token")
    @GetMapping("/v1/login/token")
    public boolean verifyToken(@RequestParam("token") String token) {
        return this.loginService.verifyToken(token);
    }

    @ApiOperation(value = "登出", notes = "用户登出，清除登录缓存")
    @DeleteMapping("/v1/logout")
    public void logout(@RequestHeader(HttpHeaders.X_LOGIN_TOKEN) String token) {
        this.loginService.logout(token);
    }

    @GetMapping("/test")
    public void testHaveUserIdHeader(@RequestHeader(HttpHeaders.X_LOGIN_USER) Long id) {
        System.out.println(id);
    }
}
