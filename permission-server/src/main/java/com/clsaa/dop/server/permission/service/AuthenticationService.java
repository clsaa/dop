package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.FeignConfig;
import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.model.dto.RoleDtoV1;
import com.clsaa.dop.server.permission.model.dto.UserRuleDtoV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *  权限管理的接口调用
 *
 * @author lzy
 *
 */

@Component
@FeignClient(value = "permission-server", configuration = FeignConfig.class)
public interface AuthenticationService {

    /* *
     *
     *  添加用户数据，一般用法：在创建一条数据时调用，自动添加用户数据
     *  * @param ruleId 规则ID
     *  * @param userId 用户ID
     *  * @param fieldValue 作用域参数值
     *  * @param loginUser 当前登录用户
     *
     */
    @PostMapping("/v1/userData")
     void addData(
            @RequestParam(value = "ruleId")Long ruleId,
            @RequestParam(value = "userId")Long userId,
            @RequestParam(value = "fieldValue")Long fieldValue,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long loginUser
    );

    /* *
     *
     *  验证登录用户是否拥有特定功能点
     *  * @param permissionName 功能点名称
     *  * @param userId 用户ID
     *
     */

    @GetMapping("/v1/users/permissionmaps")
     boolean checkUserPermission(
             @RequestParam(value = "permissionName", required = true) String permissionName,
             @RequestParam("userId") Long loginUser);

    /* *
     *
     *  查询唯一数据规则
     *  * @param rule 规则
     *  * @param fieldName 作用域参数名称
     *  * @param roleId 角色ID
     *
     */

    @GetMapping("v1/userRule")
     UserRuleDtoV1 findUniqueRule(
            @RequestParam(value = "rule") String rule,
            @RequestParam(value = "fieldName") String fieldName,
            @RequestParam(value = "roleId") Long roleId
    );

    /* *
     *
     *  通过名称查询角色
     *  * @param name 角色名称
     *
     */

    @GetMapping("/v1/roles/byName")
     RoleDtoV1 findByName(
             @RequestParam(value = "name")String name
    );

    /* *
     *
     *  得到某个功能点操作允许操作的数据范围（返回ID列表形式）
     *  * @param permissionName 功能点名称
     *  * @param userId 用户ID
     *  * @param fieldName 作用域参数名称
     *
     */

    @GetMapping("/v1/userData/byPermission")
     List<Long> findAllIds(
            @RequestParam(value = "permissionName", required = true) String permissionName,
            @RequestParam(value = "userId", required = true)Long userId,
            @RequestParam(value = "fieldName", required = true)String fieldName
    );

    /* *
     *
     *  判断该用户当前功能点是否可在该条数据执行
     *  * @param permissionName 功能点名称
     *  * @param userId 用户ID
     *  * @param fieldName 作用域参数名称
     *  * @param fieldValue 作用域参数值
     *
     */

    @GetMapping("/v1/userData")
     boolean check(
            @RequestParam(value = "permissionName", required = true) String permissionName,
            @RequestParam(value = "userId", required = true)Long userId,
            @RequestParam(value = "fieldName", required = true)String fieldName,
            @RequestParam(value = "fieldValue", required = true)Long fieldValue
    );
}

