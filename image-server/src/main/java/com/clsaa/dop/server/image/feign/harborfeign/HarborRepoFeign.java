package com.clsaa.dop.server.image.feign.harborfeign;

import com.clsaa.dop.server.image.config.FeignConfig;
import com.clsaa.dop.server.image.model.po.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用于和harbor通讯的镜像仓库接口
 * @author xzt
 * @since 2019-3-29
 */
@Component
@FeignClient(url = "${feign.url}",value = "repo",configuration = FeignConfig.class)
public interface HarborRepoFeign {

    /**
     * 通过项目id检索仓库
     * @param projectId 项目id
     * @param q 通过name查询
     * @param sort 进行排序
     * @param labelId 标签id
     * @param page 页号，默认为1
     * @param pageSize 页大小默认为10，最大为100
     * @param auth 用户认证
     * @return {@link List<Repository>}  仓库列表
     */
    @GetMapping(value = "/repositories")
    ResponseEntity<List<Repository>> repositoriesGet(@RequestParam(value = "project_id") Integer projectId,
                                     @RequestParam(value = "q", required = false) String q,
                                     @RequestParam(value = "sort", required = false) String sort,
                                     @RequestParam(value = "label_id", required = false) Integer labelId,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "page_size", required = false) Integer pageSize,
                                     @RequestHeader(value = "Authorization") String auth);

    /**
     * 通过仓库名删除镜像仓库
     * @param repoName 仓库名称
     * @param auth 用户认证
     */
    @DeleteMapping(value = "/repositories/{repo_name}")
    void repositoriesRepoNameDelete(@PathVariable("repo_name") String repoName,@RequestHeader(value = "Authorization") String auth);

    /**
     * 修改仓库信息
     * @param repoName  仓库名称
     * @param description 需要更新的仓库描述
     * @param auth 用户认证
     */
    @PutMapping(value = "/repositories/{repo_name}")
    void repositoriesRepoNamePut(@PathVariable("repo_name") String repoName, @RequestBody RepositoryDescription description,@RequestHeader(value = "Authorization") String auth);

    /**
     * 获取仓库的labels
     * @param repoName 仓库名称
     * @param auth 用户认证
     * @return {@link List<Label>} Label的列表
     */
    @GetMapping(value = "/repositories/{repo_name}/labels")
    ResponseEntity<List<Label>> repositoriesRepoNameLabelsGet(@PathVariable("repo_name") String repoName,@RequestHeader(value = "Authorization") String auth);

    /**
     * 为仓库添加label
     * @param repoName 仓库名称
     * @param label 添加的label
     * @param auth 用户认证
     */
    @PostMapping(value = "/repositories/{repo_name}/labels")
    void repositoriesRepoNameLabelsPost(@PathVariable("repo_name") String repoName,@RequestBody Label label,@RequestHeader(value = "Authorization") String auth);

    /**
     * 通过仓库名和label_id来删除label
     * @param repoName
     * @param labelId
     * @param auth 用户认证
     */
    @DeleteMapping(value = "/repositories/{repo_name}/labels/{label_id}")
    void repositoriesRepoNameLabelsLabelIdDelete(@PathVariable("repo_name") String repoName,@PathVariable("label_id") Integer labelId,@RequestHeader(value = "Authorization") String auth);

    /**
     * 通过仓库名称获取 tags
     * @param repoName 仓库名称
     * @param labelIds 标签搜索条件
     * @param auth 用户认证
     * @return {@link List<DetailedTag>} 对应的tag列表
     */
    @GetMapping(value = "/repositories/{repo_name}/tags")
    ResponseEntity<List<DetailedTag>> repositoriesRepoNameTagsGet(@PathVariable("repo_name") String repoName, @RequestParam(value = "label_ids", required = false) String labelIds,@RequestHeader(value = "Authorization") String auth);


    /**
     * 添加tag
     * @param repoName 仓库名称
     * @param retagReq 需要添加的tag
     * @param auth 用户认证
     */
    @PostMapping(value = "/repositories/{repo_name}/tags")
    void repositoriesRepoNameTagsPost(@PathVariable("repo_name") String repoName,@RequestBody RetagReq retagReq,@RequestHeader(value = "Authorization") String auth);


    /**
     * 通过镜像仓库名称和tag名称获取tag
     * @param repoName
     * @param tag
     * @param auth 用户认证
     * @return {@link DetailedTag}
     */
    @GetMapping(value = "/repositories/{repo_name}/tags/{tag}")
    DetailedTag repositoriesRepoNameTagsTagGet(@PathVariable("repo_name") String repoName,@PathVariable("tag") String tag,@RequestHeader(value = "Authorization") String auth);
    /**
     * 通过仓库名称和tag名称来删除tag
     * @param repoName 仓库名称
     * @param tagName tag名称
     * @param auth 用户认证
     */
    @DeleteMapping("/repositories/{repo_name}/tags/{tag}")
    void repositoriesRepoNameTagsTagDelete(@PathVariable("repo_name") String repoName,
                                           @PathVariable("tag") String tagName,
                                           @RequestHeader(value = "Authorization") String auth);

}
