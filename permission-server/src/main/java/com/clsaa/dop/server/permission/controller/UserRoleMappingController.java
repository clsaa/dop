package com.clsaa.dop.server.permission.controller;

import com.clsaa.dop.server.permission.config.HttpHeaders;


import com.clsaa.dop.server.permission.service.UserRoleMappingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;


/**
 * 用户角色关联管理控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param id 关联关系ID
 *  * @param userId 用户ID
 *  * @param roleId 角色ID
 *
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 *
 *since :2019.3.15
 */

@CrossOrigin
@RestController
@EnableAutoConfiguration
public class UserRoleMappingController {
    @Autowired
    private UserRoleMappingService userRoleMappingService;

    //添加一个关联关系
    @ApiOperation(value = "创建用户角色关联关系", notes = "创建用户角色关联关系")
    @PostMapping("/v1/users/rolemap")
    public void addRoleToUser(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long loginUser)
    {
        userRoleMappingService.addRoleToUser(userId,roleId,loginUser,loginUser);
    }


    @ApiOperation(value = "删除用户角色关联关系", notes = "删除用户角色关联关系")
    @DeleteMapping ("/v1/users/roles")
    //删除用户角色关联关系
    public void delete(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId)

    {
        userRoleMappingService.delete(userId,roleId);
    }


}
