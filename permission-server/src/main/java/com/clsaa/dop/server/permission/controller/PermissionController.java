package com.clsaa.dop.server.permission.controller;

import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.service.PermissionService;
import com.clsaa.dop.server.permission.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 功能点管理控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param id 功能点ID
 *  * @param parentId 父功能点ID
 *  * @param name 功能点名称
 *  * @param isPrivate 是否私有 （后期决定能否继承该功能点）
 *  * @param description 功能点描述
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 *
 *since :2019.3.2
 */
@CrossOrigin
@RestController
@EnableAutoConfiguration
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "创建功能点", notes = "创建功能点")
    @PostMapping("/v1/permissions")

    public void  createPermission(
            @ApiParam(name = "parentId",value = "父功能点ID",required = false,defaultValue = "0")
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Long parentId,
            @ApiParam(name = "name",value = "名称",required = true)
            @RequestParam(value = "name", required = true) String name,
            @ApiParam(name = "isPrivate",value = "是否私有",required = true)
            @RequestParam(value = "isPrivate", required = true) Integer isPrivate,
            @ApiParam(name = "description",value = "功能点描述",required = true)
            @RequestParam(value = "description", required = true) String description,
            @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUser
        )
    {
        permissionService.createPermission(loginUser,parentId,name,isPrivate,
                description,loginUser);
    }

    @ApiOperation(value = "根据ID查询功能点", notes = "根据ID查询功能点")
    @GetMapping("/v1/permissions/{id}")
    public PermissionV1 findById(@ApiParam(name = "id",value = "功能点ID",required = true)
                                   @RequestParam(value = "id", required = true)Long id)
    {
        return BeanUtils.convertType(this.permissionService.findById(id), PermissionV1.class);
    }

    @ApiOperation(value = "分页查询所有功能点", notes = "分页查询所有功能点")
    @GetMapping("/v1/permissions")
    public Pagination<PermissionV1> getPermissionV1Pagination(
            @ApiParam(name = "pageNo",value = "页号",defaultValue = "1")
            @RequestParam(value = "pageNo", required = false,defaultValue = "1")Integer page,
            @ApiParam(name = "pageSize",value = "页大小",defaultValue = "8")
            @RequestParam(value = "pageSize", required = false,defaultValue = "8")Integer size,
            @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(name = "key",value = "搜索关键字",defaultValue = "")
            @RequestParam(value = "key", required = false,defaultValue = "")String key)
    {
        return this.permissionService.getPermissionV1Pagination(loginUser,page,size,key);
    }
    @ApiOperation(value = "根据名称查询功能点", notes = "根据名称查询功能点")
    @GetMapping("/v1/permissions/byName")
    public PermissionV1 findByName(@ApiParam(name = "name",value = "功能点名称",required = true)
                                       @RequestParam(value = "name", required = true)String name)
    {
        return BeanUtils.convertType(this.permissionService.findByName(name), PermissionV1.class);
    }

    @ApiOperation(value="根据ID删除功能点",notes = "根据ID删除功能点")
    @DeleteMapping("v1/permissions/{id}")
    public void deleteById(@ApiParam(name = "id",value = "功能点ID",required = true)
                               @RequestParam(value = "id", required = true)Long id,
                           @RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUser
                           )
    {
        permissionService.deleteById(loginUser,id);
    }

    @ApiOperation(value = "根据角色ID查询功能点", notes = "根据角色ID查询功能点")
    @GetMapping("/v1/roles/permissions/{id}")
    //根据角色ID查询功能点
    public List<PermissionV1> findByRoleId(@ApiParam(name = "roleId",value = "角色ID",required = true)
                                           @RequestParam(value = "roleId", required = true)Long roleId)
    {
        return permissionService.findByRoleId(roleId)
                .stream().map(p -> BeanUtils.convertType(p, PermissionV1.class)).collect(Collectors.toList());
    }

    @ApiOperation(value = "根据用户ID查询功能点", notes = "根据用户ID查询功能点")
    @GetMapping("/v1/users/permissions/{id}")
    //根据用户ID查询功能点
    public List<PermissionV1> findByUserId(@ApiParam(name = "userId",value = "用户ID",required = true)
                                           @RequestParam(value = "userId", required = true)Long userId)
    {
        return permissionService.findByUserId(userId)
                .stream().map(p -> BeanUtils.convertType(p, PermissionV1.class)).collect(Collectors.toList());
    }
    @ApiOperation(value = "验证用户是否拥有特定功能点", notes = "验证用户是否拥有特定功能点")
    @GetMapping("/v1/users/permissionmaps")
    //验证登录用户是否拥有特定功能点
    public boolean checkUserPermission( @ApiParam(name = "permissionName",value = "功能点名称",required = true)
                                            @RequestParam(value = "permissionName", required = true) String permissionName,
                                        @RequestParam("userId") Long loginUser)
    {
        return permissionService.checkUserPermission(permissionName,loginUser);
    }

    @ApiOperation(value = "查询当前登录用户的功能点", notes = "查询当前登录用户的功能点")
    @GetMapping("/v1/users/permissions/ByCurrent")
    //根据用户ID查询功能点
    public List<PermissionV1> findByCurrentUserId(@RequestHeader(HttpHeaders.X_LOGIN_USER) Long loginUser)
    {
        return permissionService.findByUserId(loginUser)
                .stream().map(p -> BeanUtils.convertType(p, PermissionV1.class)).collect(Collectors.toList());
    }

}
