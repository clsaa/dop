package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.model.bo.ProjectBO;
import com.clsaa.dop.server.image.model.dto.ProjectDto1;
import com.clsaa.dop.server.image.model.po.ProjectMetadata;
import com.clsaa.dop.server.image.model.vo.ProjectVO;
import com.clsaa.dop.server.image.service.ProjectService;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *     用户项目的控制器实现类
 * </p>
 * @author xzt
 * @since 2019-3-23
 */
@RestController
@CrossOrigin
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @ApiOperation(value = "获取项目列表信息",notes = "根据权限的不同访问项目列表信息")
    @GetMapping("/v1/projects")
    public Pagination<ProjectVO> getProjects(@ApiParam(value = "项目名称") @RequestParam(value = "name", required = false) String name,
                                             @ApiParam(value = "项目的类型") @RequestParam(value = "publicStatus", required = false) Boolean publicStatus,
                                             @ApiParam(value = "项目创建人") @RequestParam(value = "owner", required = false) String owner,
                                             @ApiParam(value = "页号，默认为1") @RequestParam(value = "page", required = false) Integer page,
                                             @ApiParam(value = "页大小，默认为10，最大为100") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                             @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId){
        Pagination<ProjectBO> projectBOPagination = projectService.getProjects(name,publicStatus,owner,page,pageSize,userId);
        Pagination<ProjectVO> pagination = new Pagination<>();
        pagination.setPageNo(page);
        pagination.setPageSize(pageSize);
        pagination.setPageList(BeanUtils.convertList(projectBOPagination.getPageList(),ProjectVO.class));
        pagination.setTotalCount(projectBOPagination.getTotalCount());
        return pagination;
    }

    @ApiOperation(value = "获取某个项目的基本信息",notes = "需要给出项目的id进行查询，返回项目对象，不存在返回null")
    @GetMapping(value = "/v1/projects/{projectId}")
    public ProjectVO getProjectById(@ApiParam(value = "项目id",required = true) @PathVariable("projectId") Long projectId,
                                    @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId){
        return BeanUtils.convertType(projectService.getProjectById(projectId,userId),ProjectVO.class);
    }

    @ApiOperation(value = "新建项目",notes = "需要给出项目的基本信息，返回http状态码")
    @PostMapping(value = "/v1/projects")
    public void addProject(@ApiParam(value = "项目名称",required = true) @RequestParam(value = "name") String projectName,
                           @ApiParam(value = "项目权限",required = true) @RequestParam(value = "status") String publicStatus,
                           @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId){
        ProjectMetadata metadata = new ProjectMetadata(publicStatus,"false","false","low","false");
        ProjectDto1 projectDto1 = new ProjectDto1(projectName,metadata);
        projectService.addProject(projectDto1,userId);
    }

    @ApiOperation(value = "修改项目信息",notes = "根据项目id，来更改项目信息，返回http状态码")
    @PutMapping(value = "/v1/projects/{projectId}")
    public void putProject(@ApiParam(value = "项目 id",required = true) @PathVariable(value = "projectId")Long projectId,
                           @ApiParam(value = "项目内容",required = true) @RequestBody ProjectDto1 projectDto1,
                           @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId){
        projectService.putProject(projectId,projectDto1,userId);
    }

    @ApiOperation(value = "项目删除" ,notes = "根据项目的id删除项目，返回http状态码")
    @DeleteMapping(value = "/v1/projects/{projectId}")
    public void deleteProject(@ApiParam(value = "项目id",required = true) @PathVariable("projectId")Long projectId,
                              @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId){
        projectService.deleteProject(projectId,userId);
    }

    @ApiOperation(value = "修改的项目的公开属性",notes = "根据项目id来对项目的基本信息进行修改")
    @PutMapping(value = "/v1/projects/{projectId}/metadatas/{metaName}")
    public void updatePublicStatus(@ApiParam(value = "项目id",required = true)@PathVariable(value = "projectId") Long projectId,
                                   @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId,
                                   @ApiParam(value = "属性名称",required = true) @PathVariable(value = "metaName")String metaName,
                                   @ApiParam(value = "修改状态",required = true) @RequestParam(value = "publicStatus") String publicStatus){
        projectService.updatePublicStatus(projectId,metaName,userId,publicStatus);
    }

}
