package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.model.vo.ProjectV1;
import com.clsaa.dop.server.application.model.vo.UserV1;
import com.clsaa.dop.server.application.service.ProjectService;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.List;


/**
 * <p>
 * 项目API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-5
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "查询项目信息", notes = "查询项目信息, never return null")
    @GetMapping(value = "/project/{projectId}")
    public ProjectV1 findProjectById(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
                                     @PathVariable(value = "projectId") Long projectId) {

        return BeanUtils.convertType(this.projectService.findProjectById(loginUser, projectId), ProjectV1.class);
    }

    @ApiOperation(value = "分页查询项目信息", notes = "分页查询项目信息, never return null")
    @GetMapping(value = "/paged-project")
    public Pagination<ProjectV1> findProjectOrderByCtimeWithPage(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
                                                                 @ApiParam(name = "pageNo", value = "页号", required = true, defaultValue = "1") @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @ApiParam(name = "pageSize", value = "页大小", required = true, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 @ApiParam(name = "includeFinished", value = "是否包含已结项目", required = true, defaultValue = "false") @RequestParam(value = "includeFinished", defaultValue = "false") Boolean includeFinished,
                                                                 @ApiParam(name = "queryKey", value = "搜索关键字", defaultValue = "") @RequestParam(value = "queryKey", defaultValue = "") String queryKey) {

        return this.projectService.findProjectOrderByCtimeWithPage(loginUser, pageNo, pageSize, includeFinished, queryKey);
    }

    @ApiOperation(value = "查询项目的成员", notes = "查询项目的成员")
    @GetMapping(value = "/project/{projectId}/members")
    public List<UserV1> getMemberInProject(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
                                           @PathVariable(value = "projectId") Long projectId,
                                           @ApiParam(name = "organizationId", value = "组织名称", required = true) @RequestParam(value = "organizationId") Long organizationId) {
        return this.projectService.getMembersInProject(projectId);

    }

    @ApiOperation(value = "添加项目的成员", notes = "添加项目的成员")
    @PostMapping(value = "/project/{projectId}/members")
    public void addMemberToProject(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
                                   @PathVariable(value = "projectId") Long projectId,
                                   //@RequestBody UserIdListV1 userIdList,
                                   @ApiParam(name = "userIdList", value = "userIdList", required = true) @RequestParam(value = "userIdList") List<Long> userIdList,
                                   @ApiParam(name = "organizationId", value = "组织名称", required = true) @RequestParam(value = "organizationId") Long organizationId) {

        this.projectService.addMemberToProject(userIdList, projectId, loginUser);

    }

    @ApiOperation(value = "删除项目的成员", notes = "删除项目的成员")
    @DeleteMapping(value = "/project/{projectId}/members")
    public void deleteMemberFromProject(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
                                        @PathVariable(value = "projectId") Long projectId,
                                        @ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") Long userId,
                                        @ApiParam(name = "organizationId", value = "组织名称", required = true) @RequestParam(value = "organizationId") Long organizationId) {
        this.projectService.deleteMemberFromProject(userId, projectId, loginUser);

    }


    @ApiOperation(value = "创建项目", notes = "创建项目")
    @PostMapping(value = "/project")
    public void createProject(@RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
                              @ApiParam(name = "title", value = "项目名称", required = true) @RequestParam(value = "title") String title,
                              @ApiParam(name = "organizationId", value = "组织名称", required = true) @RequestParam(value = "organizationId") Long organizationId,
                              @ApiParam(name = "projectDescription", value = "项目描述", defaultValue = "") @RequestParam(value = "projectDescription", required = false) String projectDescription,
                              @ApiParam(name = "status", value = "项目状态", defaultValue = "") @RequestParam(value = "status") String status) {

        BizAssert.validParam(projectDescription == null || projectDescription.length() < 50,
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "描述长度必须小于50"));
        this.projectService.createProjects(loginUser, title, organizationId, projectDescription, status);

    }


}