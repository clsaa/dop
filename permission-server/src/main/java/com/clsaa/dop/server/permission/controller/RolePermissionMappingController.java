package com.clsaa.dop.server.permission.controller;

import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.service.RolePermissionMappingService;
import com.clsaa.dop.server.permission.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;


/**
 * 角色功能点关联管理控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param id 关联关系ID
 *  * @param roleId 角色ID
 *  * @param permissionId 功能点ID
 *
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 *
 *since :2019.3.11
 */

@CrossOrigin
@RestController
@EnableAutoConfiguration
public class RolePermissionMappingController {
    @Autowired
    private RolePermissionMappingService rolePermissionMappingService;

    //添加一个关联关系
    @ApiOperation(value = "创建角色功能点关联关系", notes = "创建角色功能点关联关系")
    @PostMapping("/v1/roles/permissionmap")
    public void addPermissionToRole(
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId,
            @ApiParam(name = "permissionId",value = "功能点ID",required = true)
            @RequestParam(value = "permissionId", required = true)Long permissionId,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long loginUser)
    {
            rolePermissionMappingService.addPermissionToRole(roleId,permissionId,loginUser,loginUser);
    }


    @ApiOperation(value = "删除角色功能点关联关系", notes = "删除角色功能点关联关系")
    @DeleteMapping ("/v1/roles/permissions")
    //删除角色功能点关联关系
    public void delete(
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId,
            @ApiParam(name = "permissionId",value = "功能点ID",required = true)
            @RequestParam(value = "permissionId", required = true)Long permissionId)
    {
        rolePermissionMappingService.delete(roleId,permissionId);
    }


}
