package com.clsaa.dop.server.application.feign;

import com.clsaa.dop.server.application.config.FeignConfig;
import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(value = "permission-server", configuration = FeignConfig.class)
public interface PermissionFeign {

    /**
     * 查询用户是否拥有某权限
     *
     * @return boolean
     */
    @GetMapping("/v1/users/permissionmaps")
    boolean permissionCheck(@RequestParam(value = "permissionName") String permissionName,
                            @RequestParam("userId") Long loginUser);


    /**
     * 查询数据权限
     *
     * @return boolean
     */
    @GetMapping("/v1/userData")
    boolean check(
            @ApiParam(name = "permissionName", value = "功能点名称", required = true)
            @RequestParam(value = "permissionName", required = true) String permissionName,
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam(value = "userId", required = true) Long userId,
            @ApiParam(name = "fieldName", value = "作用域参数名称", required = true)
            @RequestParam(value = "fieldName", required = true) String fieldName,
            @ApiParam(name = "fieldValue", value = "作用域参数值", required = true)
            @RequestParam(value = "fieldValue", required = true) Long fieldValue
    );

    /**
     * 查询数据权限
     *
     * @return boolean
     */
    @GetMapping("/v1/userData/byField")
    List<Long> findUserByField(@ApiParam(name = "fieldName", value = "作用域参数名称", required = true)
                               @RequestParam(value = "fieldName") String fieldName,
                               @ApiParam(name = "fieldValue", value = "作用域参数值", required = true)
                               @RequestParam(value = "fieldValue") Long fieldValue
    );

    @PostMapping("/v1/userData")
    void addData(
            @ApiParam(name = "ruleId", value = "规则ID", required = true)
            @RequestParam(value = "ruleId", required = true) Long ruleId,
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam(value = "userId", required = true) Long userId,
            @ApiParam(name = "fieldValue", value = "权限作用域参数值", required = true)
            @RequestParam(value = "fieldValue", required = true) Long fieldValue,
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser
    );

    //得到某个功能点操作允许操作的数据范围（返回ID列表形式）
    @GetMapping("/v1/userData/byPermission")
    public List<Long> findAllIds(
            @ApiParam(name = "permissionName", value = "功能点名称", required = true)
            @RequestParam(value = "permissionName", required = true) String permissionName,
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam(value = "userId", required = true) Long userId,
            @ApiParam(name = "fieldName", value = "作用域参数名称", required = true)
            @RequestParam(value = "fieldName", required = true) String fieldName
    );

    @ApiOperation(value = "根据字段和用户ID删除用户可操作的数据", notes = "根据字段和用户ID删除用户可操作的数据")
    @DeleteMapping("/v1/userData/byFieldAndUserId")
    public void deleteByFieldAndUserId(
            @ApiParam(name = "fieldValue", value = "作用域值", required = true)
            @RequestParam(value = "fieldValue") Long fieldValue,
            @ApiParam(name = "fieldName", value = "作用域名称", required = true)
            @RequestParam(value = "fieldName") String fieldName,
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam(value = "userId") Long userId
    );

    ////添加一个关联关系
    //@ApiOperation(value = "创建用户角色关联关系", notes = "创建用户角色关联关系")
    //@PostMapping("/v1/users/rolemap")
    // void addRoleToUser(
    //        @ApiParam(name = "userId",value = "用户ID",required = true)
    //        @RequestParam(value = "userId", required = true)Long userId,
    //        @ApiParam(name = "roleId",value = "角色ID",required = true)
    //        @RequestParam(value = "roleId", required = true)Long roleId,
    //        @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER)Long loginUser);



}
