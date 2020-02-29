package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.model.bo.RepositoryBO;
import com.clsaa.dop.server.image.model.vo.RepositoryVO;
import com.clsaa.dop.server.image.service.RepositoryService;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 用于管理镜像仓库的控制类
 * @author 2019-3-25
 */
@RestController
@CrossOrigin
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @ApiOperation(value = "通过项目id获取某个项目的镜像仓库")
    @GetMapping(value = "/v1/projects/{projectId}/repositories")
    public Pagination<RepositoryVO> getRepositories(@ApiParam(value = "项目id",required = true) @PathVariable(value = "projectId") Integer projectId,
                                                    @ApiParam(value = "搜索条件") @RequestParam(value = "q", required = false) String q,
                                                    @ApiParam(value = "排序方式") @RequestParam(value = "sort", required = false) String sort,
                                                    @ApiParam(value = "标签id") @RequestParam(value = "labelId", required = false) Integer labelId,
                                                    @ApiParam(value = "页号，默认为1") @RequestParam(value = "page", required = false) Integer page,
                                                    @ApiParam(value = "页大小，默认为10，最大为100") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                    @ApiParam(value = "用户id") @RequestHeader(value = "x-login-user")Long userId){

        Pagination<RepositoryBO> pagination = repositoryService.getRepositories(projectId,q,sort,labelId,page,pageSize,userId);
        Pagination<RepositoryVO> pagination1 = new Pagination<>();
        pagination1.setTotalCount(pagination.getTotalCount());
        pagination1.setPageNo(page);
        pagination1.setPageSize(pageSize);
        pagination1.setPageList(BeanUtils.convertList(pagination.getPageList(),RepositoryVO.class));
        return pagination1;
    }

    @ApiOperation(value = "删除镜像仓库")
    @DeleteMapping(value = "/v1/repositories/{projectName}/{repoName}")
    public void deleteRepository(@ApiParam(value = "项目名称") @PathVariable(value = "projectName") String projectName,
                                 @ApiParam(value = "镜像仓库名称") @PathVariable(value = "repoName")String repoName,
                                 @ApiParam(value = "用户id") @RequestHeader(value = "x-login-user")Long userId){
        repositoryService.deleteRepository(projectName,repoName,userId);
    }

    @ApiOperation(value = "获取镜像仓库地址")
    @GetMapping(value = "/v1/repoAddress")
    public List<String> getRepoAddress(@ApiParam(value = "用户id") @RequestHeader(value = "x-login-user")Long userId){
        return repositoryService.getRepoAddress(userId);
    }


}
