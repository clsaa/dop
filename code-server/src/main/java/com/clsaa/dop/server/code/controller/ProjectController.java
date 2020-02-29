package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.project.MemberBo;
import com.clsaa.dop.server.code.model.bo.project.ProjectListBo;
import com.clsaa.dop.server.code.model.dto.project.ProjectDto;
import com.clsaa.dop.server.code.model.vo.project.*;
import com.clsaa.dop.server.code.service.ProjectService;
import com.clsaa.dop.server.code.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@CrossOrigin
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @ApiOperation(value = "获得用户可以拉取代码的所有项目地址",notes = "根据用户的id获得可以拉取代码的所有项目地址，包括所有的public项目，和权限在guest以上的项目" )
    @GetMapping("/project_url_list")
    public List<String> findProjectUrlList(@ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        return projectService.findProjectUrlList(userId);
    }

    @ApiOperation(value = "查询项目信息",notes="根据项目的id查询项目总览需要的信息")
    @GetMapping("/projects/{username}/{projectname}")
    public ProjectVo findProject(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                 @ApiParam(value = "项目名") @PathVariable("projectname") String projectname){
        String id=username+"/"+projectname;
        return BeanUtils.convertType(projectService.findProject(id),ProjectVo.class);
    }

    @ApiOperation(value = "star一个项目",notes = "若项目没有star则star,否则unstar")
    @PostMapping("/projects/{username}/{projectname}/star")
    public int starProject(@ApiParam(value = "用户名") @PathVariable("username") String username,
                           @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                           @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        return projectService.starProject(id,userId);
    }

    @ApiOperation(value = "查找用户参与的项目",notes = "根据用户名查找用户参与的项目")
    @GetMapping("/projects")
    public List<ProjectListVo> findProjectList(@ApiParam(value = "分类")@RequestParam("sort")String sort,
                                               @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        List<ProjectListBo> listBos= projectService.findProjectList(sort,userId);
        List<ProjectListVo> listVos=new ArrayList<>();
        for(ProjectListBo temp:listBos)
            listVos.add(BeanUtils.convertType(temp,ProjectListVo.class));
        return listVos;
    }

    @ApiOperation(value="新建一个项目",notes = "新建一个项目，包括一些基本信息")
    @PostMapping("/projects")
    public void addProject(@ApiParam(value = "项目基本信息")@RequestBody ProjectDto projectDto){
        projectService.addProject(
                projectDto.getName(),
                projectDto.getDescription(),
                projectDto.getVisibility(),
                projectDto.getInitialize_with_readme(),
                projectDto.getUserId()
        );
    }

    @ApiOperation(value = "查询编辑项目需要的信息",notes = "根据项目id查询")
    @GetMapping("/projects/{username}/{projectname}/editinfo")
    public ProjectEditVo findProjectEditInfo(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                             @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                             @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        return BeanUtils.convertType(projectService.findProjectEditInfo(id,userId),ProjectEditVo.class);
    }

    @ApiOperation(value = "获得项目所有的分支名",notes = "根据项目id查询")
    @GetMapping("/projects/{username}/{projectname}/branches")
    public List<BranchVo> findAllBranchName(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                            @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                            @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<String> strs=projectService.findAllBranchName(id,userId);

        List<BranchVo> branchVos=new ArrayList<>();
        for(String str:strs){
            branchVos.add(new BranchVo(str,str));
        }

        return branchVos;
    }

    @ApiOperation(value = "编辑项目信息",notes = "编辑项目信息包括名称、描述、默认分支和可见等级")
    @PutMapping("/projects/{username}/{projectname}")
    public void editProjectInfo(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                @ApiParam(value = "项目名称")@RequestParam("name") String name,
                                @ApiParam(value = "项目描述")@RequestParam("description") String description,
                                @ApiParam(value = "默认分支")@RequestParam("default_branch") String default_branch,
                                @ApiParam(value = "可见等级")@RequestParam("visibility") String visibility,
                                @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        projectService.editProjectInfo(id,name,description,default_branch,visibility,userId);
    }

    @ApiOperation(value = "删除一个项目",notes = "根据项目id删除")
    @DeleteMapping("/projects/{username}/{projectname}")
    public void deleteProject(@ApiParam(value = "用户名") @PathVariable("username") String username,
                              @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                              @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        projectService.deleteProject(id,userId);
    }

    @ApiOperation(value = "获得项目的默认分支名",notes = "根据项目id")
    @GetMapping("/projects/{username}/{projectname}/defaultbranch")
    public String findProjectDefaultBranch(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                           @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                           @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        return projectService.findProjectDefaultBranch(id,userId);
    }



    @ApiOperation(value = "获得项目的成员列表",notes = "根据项目id获得项目的成员列表")
    @GetMapping("/projects/{username}/{projectname}/members")
    public List<MemberVo> findProjectMemberList(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                                @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                                @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<MemberBo> memberBos= projectService.findProjectMemberList(id,userId);
        List<MemberVo> memberVos=new ArrayList<>();
        for(MemberBo memberBo:memberBos){
            memberVos.add(BeanUtils.convertType(memberBo,MemberVo.class));
        }
        return memberVos;
    }

    @ApiOperation(value = "增加一个项目成员",notes = "根据项目id，用户名增加一个项目成员并设置权限等级")
    @PostMapping("/projects/{username}/{projectname}/members")
    public void addProjectMember(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                 @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                 @ApiParam(value = "gitlab用户名") @RequestParam("user_name") String user_name,
                                 @ApiParam(value = "权限等级") @RequestParam("access_level") int access_level,
                                 @ApiParam(value = "dop用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        projectService.addProjectMember(id,user_name,access_level,userId);
    }

    @ApiOperation(value = "修改项目成员的权限等级",notes = "根据项目id，用户id设置权限等级")
    @PutMapping("/projects/{username}/{projectname}/members")
    public void changeProjectMemberAccessLevel(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                 @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                 @ApiParam(value = "gitlab用户id") @RequestParam("user_id") int user_id,
                                 @ApiParam(value = "权限等级") @RequestParam("access_level") int access_level,
                                 @ApiParam(value = "dop用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        projectService.changeProjectMemberAccessLevel(id,user_id,access_level,userId);
    }

    @ApiOperation(value = "删除一个项目成员",notes = "根据项目id，用户id删除项目成员")
    @DeleteMapping("/projects/{username}/{projectname}/members")
    public void deleteProjectMember(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                               @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                               @ApiParam(value = "gitlab用户id") @RequestParam("user_id") int user_id,
                                               @ApiParam(value = "dop用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        projectService.deleteProjectMember(id,user_id,userId);
    }

    @ApiOperation(value = "查询用户的项目权限",notes = "根据项目id，用户id查询用户的项目权限包括项目可见等级和用户的角色")
    @GetMapping("/projects/{username}/{projectname}/access")
    public ProjectAccessLevelVo findProjectAccessLevel(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                                       @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                                       @ApiParam(value = "dop用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        return BeanUtils.convertType(projectService.findProjectAccessLevel(id,userId),ProjectAccessLevelVo.class);
    }






}
