package com.clsaa.dop.server.image.controller;


import com.clsaa.dop.server.image.model.bo.ProjectMemberBO;
import com.clsaa.dop.server.image.model.vo.ProjectMemberVO;
import com.clsaa.dop.server.image.service.ProjectMemberService;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目成员的控制器
 * @author xzt
 * @since 2019-4-18
 */
@RestController
@CrossOrigin
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @Autowired
    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @GetMapping(value = "/v1/projects/{projectId}/members")
    @ApiOperation(value = "获取项目成员的分页信息" )
    public Pagination<ProjectMemberVO> getMembers(@ApiParam(value = "项目id",required = true) @PathVariable(value = "projectId")Long projectId,
                                                  @ApiParam(value = "页号",required = true) @RequestParam(value = "pageNo")Integer pageNo,
                                                  @ApiParam(value = "页大小",required = true) @RequestParam(value = "pageSize") Integer pageSize,
                                                  @ApiParam(value = "搜索条件用户名称") @RequestParam(value = "entityName",required = false) String entityName,
                                                  @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user") Long userId){
        Pagination<ProjectMemberVO> pagination = new Pagination<>();
        Pagination<ProjectMemberBO> pagination1 = projectMemberService.getProjectMembers(pageNo,pageSize,projectId,entityName,userId);
        pagination.setPageList(BeanUtils.convertList(pagination1.getPageList(),ProjectMemberVO.class));
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.setTotalCount(pagination1.getTotalCount());
        return pagination;
    }

    @PostMapping(value = "/v1/projects/{projectId}/members")
    @ApiOperation(value = "将用户添加到项目")
    public void addMember(@ApiParam(value = "项目id",required = true) @PathVariable(value = "projectId")Integer projectId,
                          @ApiParam(value = "用户名称",required = true) @RequestParam(value = "userName") String userName,
                          @ApiParam(value = "角色id",required = true) @RequestParam(value = "roleId") Integer roleId,
                          @ApiParam(value = "登录用户id",required = true) @RequestHeader(value = "x-login-user") Long userId){
        projectMemberService.addMember(projectId,userName,roleId,userId);
    }

    @DeleteMapping(value = "/v1/projects/{projectId}/members/{mid}")
    @ApiOperation(value = "将项目成员从项目中移除")
    public void deleteMember(@ApiParam(value = "项目id",required = true) @PathVariable(value = "projectId") Integer projectId,
                             @ApiParam(value = "成员id",required = true) @PathVariable(value = "mid")Long mid,
                             @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId){
        projectMemberService.deleteMember(projectId,mid,userId);
    }

    @PutMapping(value = "/v1/projects/{projectId}/members/{mid}")
    @ApiOperation(value = "修改成员的角色")
    public void putMember(@ApiParam(value = "项目id",required = true) @PathVariable(value = "projectId") Integer projectId,
                          @ApiParam(value = "成员id",required = true) @PathVariable(value = "mid")Long mid,
                          @ApiParam(value = "角色id",required = true) @RequestParam(value = "roleId")Integer roleId,
                          @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId){
        projectMemberService.putMember(projectId,mid,roleId,userId);
    }


}

