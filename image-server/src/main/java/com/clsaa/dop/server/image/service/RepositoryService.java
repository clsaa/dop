package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.HarborRepoFeign;
import com.clsaa.dop.server.image.model.bo.ProjectBO;
import com.clsaa.dop.server.image.model.bo.RepositoryBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.Repository;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 镜像仓库服务类
 * @author  xzt
 * @since 2019-3-30
 */
@Service
public class RepositoryService {

    private final HarborRepoFeign harborRepoFeign;

    private final UserFeign userFeign;

    private final ProjectService projectService;

    @Autowired
    public RepositoryService(HarborRepoFeign harborRepoFeign, UserFeign userFeign,ProjectService projectService) {
        this.harborRepoFeign = harborRepoFeign;
        this.userFeign = userFeign;
        this.projectService = projectService;
    }

    /**
     * 获取对应的仓库信息
     * @param projectId 项目id
     * @param q 检索条件
     * @param sort 排序方式
     * @param labelId 标签id
     * @param page 页号
     * @param pageSize 页大小
     * @param userId 用户id
     * @return {@link List<RepositoryBO>} 镜像仓库的列表
     */
    public Pagination<RepositoryBO> getRepositories(Integer projectId, String q, String sort, Integer labelId, Integer page, Integer pageSize, Long userId){
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);

        ResponseEntity<List<Repository>> responseEntity = harborRepoFeign.repositoriesGet(projectId,q,sort,labelId,page,pageSize,auth);
        List<Repository> repositories = responseEntity.getBody();

        int count = 0;
        List<String> httpHeader = responseEntity.getHeaders().get("X-Total-Count");
        if (httpHeader!=null){
            count = Integer.parseInt(httpHeader.get(0));
        }

        Pagination<RepositoryBO> pagination = new Pagination<>();
        pagination.setTotalCount(count);
        pagination.setPageSize(pageSize);
        pagination.setPageNo(page);


        if (count==0){
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }else {
            List<RepositoryBO> repositoryBOS = new ArrayList<>();
            for (Repository repository:repositories){
                RepositoryBO repositoryBO = BeanUtils.convertType(repository,RepositoryBO.class);
                repositoryBOS.add(repositoryBO);
            }
            pagination.setPageList(repositoryBOS);
            return pagination;
        }
    }

    /**
     *
     * @param projectName 项目id
     * @param repoName 镜像仓库名称
     * @param userId 用户id
     */
    public void deleteRepository(String projectName,String repoName,Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        String repo = projectName+"/"+repoName;
        System.out.println(repo);
        harborRepoFeign.repositoriesRepoNameDelete(repo,auth);
    }

    public List<String> getRepoAddress(Long userId){

        List<String> list = new ArrayList<>();
        String beginAddress = "registry.dop.clsaa.com/";
        //获取用户可访问的项目
        List<Integer> projectIds = new ArrayList<>();
        int pageNo = 1;
        Pagination<ProjectBO> pagination = new Pagination<>();
        for (;!pagination.isLastPage();pageNo++){
            pagination = projectService.getProjects(null,null,null,pageNo,10,userId);
            for (ProjectBO projectBO:pagination.getPageList()){
                projectIds.add(projectBO.getProjectId());
            }
        }

        for (int i=0;i<projectIds.size();i++){
            Integer projectId = projectIds.get(i);

            Pagination<RepositoryBO> pagination1 = getRepositories(projectId,"","",null,1,10,userId);
            int totalCount = pagination1.getTotalCount();
            //不是最后一页的处理
            while (!pagination1.isLastPage()){
                for (int begin =0;begin<pagination1.getPageSize();begin++){
                    String address = beginAddress+pagination1.getPageList().get(begin).getName();
                    list.add(address);
                }
                pagination1 = getRepositories(projectId,"","",null,pagination1.getNextPage(),10,userId);
            }
            //对于最后一页的处理
            int lastCount = totalCount - (pagination1.getPageNo()-1)*pagination1.getPageSize();
            for (int begin=0;begin<lastCount;begin++){
                String address = beginAddress+pagination1.getPageList().get(begin).getName();
                list.add(address);
            }
        }
        return  list;
    }
}
