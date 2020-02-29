package com.clsaa.dop.server.permission.controller;


import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.model.vo.UserDataV1;
import com.clsaa.dop.server.permission.service.UserDataService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户数据控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param ruleId 规则ID
 *  * @param userId 用户ID
 *  * @param fieldValue 作用域参数值
 *
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 *
 *since :2019.3.19
 */

@CrossOrigin
@RestController
@EnableAutoConfiguration
public class UserDataController {
    @Autowired
    private UserDataService userDataService;

    //添加一个用户数据
    @ApiOperation(value = "添加一个用户数据", notes = "添加一个用户数据")
    @PostMapping("/v1/userData")
    public void addData(
            @ApiParam(name = "ruleId",value = "规则ID",required = true)
            @RequestParam(value = "ruleId")Long ruleId,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId")Long userId,
            @ApiParam(name = "fieldValue",value = "权限作用域参数值",required = true)
            @RequestParam(value = "fieldValue")Long fieldValue,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long loginUser
    )
    {
        userDataService.addData(ruleId,userId,fieldValue,loginUser,loginUser);
    }

    //添加多个用户数据
    @ApiOperation(value = "为多个用户添加数据", notes = "为多个用户添加数据")
    @PostMapping("/v1/userData/byUserList")
    public void addDataByUserList(
            @ApiParam(name = "ruleId",value = "规则ID",required = true)
            @RequestParam(value = "ruleId")Long ruleId,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId")List<Long> userIdList,
            @ApiParam(name = "fieldValue",value = "权限作用域参数值",required = true)
            @RequestParam(value = "fieldValue")Long fieldValue,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long loginUser
    )
    {
        userDataService.addDataByUserList(ruleId,userIdList,fieldValue,loginUser,loginUser);
    }

    //判断该用户当前功能点是否可在该条数据执行
    @ApiOperation(value = "验证某个功能点操作的数据是否允许操作", notes = "验证某个功能点操作的数据是否允许操作")
    @GetMapping("/v1/userData")
    public boolean check(
            @ApiParam(name = "permissionName",value = "功能点名称",required = true)
            @RequestParam(value = "permissionName", required = true) String permissionName,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "fieldName",value = "作用域参数名称",required = true)
            @RequestParam(value = "fieldName", required = true)String fieldName,
            @ApiParam(name = "fieldValue",value = "作用域参数值",required = true)
            @RequestParam(value = "fieldValue", required = true)Long fieldValue
    )
    {
      return   userDataService.check(permissionName,userId,fieldName,fieldValue);
    }

    //得到某个功能点操作允许操作的数据范围（返回ID列表形式）
    @ApiOperation(value = "得到某个功能点操作允许操作的数据范围（返回ID列表形式）", notes = "得到某个功能点操作允许操作的数据范围（返回ID列表形式）")
    @GetMapping("/v1/userData/byPermission")
    public List<Long> findAllIds(
            @ApiParam(name = "permissionName",value = "功能点名称",required = true)
            @RequestParam(value = "permissionName", required = true) String permissionName,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true)Long userId,
            @ApiParam(name = "fieldName",value = "作用域参数名称",required = true)
            @RequestParam(value = "fieldName", required = true)String fieldName
    )
    {
       return userDataService.findAllIds(permissionName,userId,fieldName);
    }

    //根据用户ID和关键字查询数据
    @ApiOperation(value = "根据用户ID查询数据", notes = "根据用户ID查询数据")
    @GetMapping("/v1/userData/byUser")
    public List<UserDataV1> findByUserId(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId", required = true) Long userId,
            @ApiParam(name = "key",value = "搜索关键字",required = false,defaultValue = "")
            @RequestParam(value = "key", required = false,defaultValue = "")String key)
    {
        return userDataService.findByUserId(userId,key);
    }

    //根据ID删除数据
    @ApiOperation(value = "根据ID删除数据", notes = "根据ID删除数据")
    @DeleteMapping("/v1/userData/{id}")
    public void deleteById(
            @ApiParam(name = "id",value = "ID",required = true)
            @RequestParam(value = "id", required = true) Long id
    )
    {
        userDataService.deleteById(id);
    }

    //根据字段值查找用户ID列表
    @ApiOperation(value = "根据字段值查找用户ID列表", notes = "根据字段值查找用户ID列表")
    @GetMapping("/v1/userData/byField")
    public List<Long> findUserByField(
            @ApiParam(name = "fieldValue",value = "作用域值",required = true)
            @RequestParam(value = "fieldValue") Long fieldValue,
            @ApiParam(name = "fieldName",value = "作用域名称",required = true)
            @RequestParam(value = "fieldName") String fieldName
    )
    {
       return userDataService.findUserByField(fieldValue,fieldName);
    }

    //根据字段和用户ID删除用户可操作的数据
    @ApiOperation(value = "根据字段和用户ID删除用户可操作的数据", notes = "根据字段和用户ID删除用户可操作的数据")
    @DeleteMapping("/v1/userData/byFieldAndUserId")
    public void deleteByFieldAndUserId(
            @ApiParam(name = "fieldValue",value = "作用域值",required = true)
            @RequestParam(value = "fieldValue") Long fieldValue,
            @ApiParam(name = "fieldName",value = "作用域名称",required = true)
            @RequestParam(value = "fieldName") String fieldName,
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam(value = "userId") Long userId
    )
    {
       userDataService.deleteByFieldAndUserId(fieldValue,fieldName,userId);
    }


}
