package com.clsaa.dop.server.permission.controller;

import com.clsaa.dop.server.permission.config.HttpHeaders;
import com.clsaa.dop.server.permission.model.bo.UserRuleBoV1;
import com.clsaa.dop.server.permission.model.po.UserRule;
import com.clsaa.dop.server.permission.model.vo.RoleV1;
import com.clsaa.dop.server.permission.model.vo.UserRuleV1;
import com.clsaa.dop.server.permission.service.UserRuleService;
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
 * 用户数据规则控制层
 *
 * @author lzy
 *
 *   /*
 *  * @param roleId 角色ID
 *  * @param fieldName 权限作用域参数名
 *  * @param rule 数据规则
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
public class UserRuleController {
    @Autowired
    private UserRuleService userRuleService;

    //添加一个用户数据规则
    @ApiOperation(value = "添加一个用户数据规则", notes = "添加一个用户数据规则")
    @PostMapping("/v1/userRules")
    public void addRule(
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId,
            @ApiParam(name = "fieldName",value = "权限作用域参数名",required = true)
            @RequestParam(value = "fieldName", required = true)String fieldName,
            @ApiParam(name = "rule",value = "规则",required = true)
            @RequestParam(value = "rule", required = true)String rule,
            @RequestHeader(HttpHeaders.X_LOGIN_USER)Long loginUser
    )
    {
        userRuleService.addRule(roleId,fieldName,rule,loginUser,loginUser);
    }

    @ApiOperation(value = "分页查询所有规则", notes = "分页查询所有规则")
    @GetMapping("/v1/userRules")
    public Pagination<UserRuleV1> getRoleV1Pagination(
            @ApiParam(name = "pageNo",value = "页号",required = false,defaultValue = "1")
            @RequestParam(value = "pageNo", required = false,defaultValue = "1")Integer page,
            @ApiParam(name = "pageSize",value = "页大小",required = false,defaultValue = "8")
            @RequestParam(value = "pageSize", required = false,defaultValue = "8")Integer size)
    {
        return this.userRuleService.getUserRuleV1Pagination(page,size);
    }

    //根据角色ID查找规则
    @ApiOperation(value = "根据角色ID查找规则", notes = "根据角色ID查找规则")
    @GetMapping("/v1/userRules?roleId=:roleId")
    public List<UserRuleV1> findByRoleId(
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId", required = true)Long roleId
    )
    {
        return userRuleService.findByRoleId(roleId).stream().map(p -> BeanUtils.convertType(p, UserRuleV1.class)).collect(Collectors.toList());
    }

    //根据ID删除规则
    @ApiOperation(value = "根据ID删除规则", notes = "根据ID删除规则")
    @DeleteMapping("v1/userRules/{id}")
    public void deleteById(
            @ApiParam(name = "id",value = "规则ID",required = true)
            @RequestParam(value = "id", required = true)Long id)
    {
        userRuleService.deleteById(id);
    }

    //查询唯一规则
    @ApiOperation(value = "查询唯一规则", notes = "查询唯一规则")
    @GetMapping("v1/userRule")
    public UserRuleV1 findUniqueRule(
            @ApiParam(name = "rule",value = "规则",required = true)
            @RequestParam(value = "rule") String rule,
            @ApiParam(name = "fieldName",value = "作用域参数名称",required = true)
            @RequestParam(value = "fieldName") String fieldName,
            @ApiParam(name = "roleId",value = "角色ID",required = true)
            @RequestParam(value = "roleId") Long roleId
   )
    {
       return BeanUtils.convertType(userRuleService.findUniqueRule(rule,fieldName,roleId),UserRuleV1.class);
    }

}
