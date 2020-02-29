package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.model.vo.UserV1;
import com.clsaa.dop.server.application.service.UserService;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * <p>
 * 用户API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-4-27
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据id查询用户", notes = "根据id查询用户")
    @GetMapping(value = "/userInfo")
    public UserV1 findUserByUserId(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") Long userId) {
        return this.userService.findUserById(userId);
    }

    @ApiOperation(value = "查询所有用户", notes = "查询所有用户")
    @GetMapping(value = "/all_users")
    public Pagination<UserV1> findUsersNotInProject(
            @ApiParam(value = "关键字，姓名或邮箱") @RequestParam(value = "key", required = false) String key,
            @ApiParam(name = "pageNo", value = "页号", required = true, defaultValue = "1") @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @ApiParam(name = "pageSize", value = "页大小", required = true, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return this.userService.findUsersNotInProject(key, pageNo, pageSize);
    }

}
