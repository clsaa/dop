package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.ProjectFeign;
import com.clsaa.dop.server.image.model.bo.ProjectMemberBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.*;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 项目成员的业务实现
 * @author  xzt
 * @since 2019-4-18
 */
@Service
public class ProjectMemberService {
    private final ProjectFeign projectFeign;

    private final UserFeign userFeign;

    @Autowired
    public ProjectMemberService(ProjectFeign projectFeign, UserFeign userFeign) {
        this.projectFeign = projectFeign;
        this.userFeign = userFeign;
    }

    /**
     * 获取项目成员分页列表
     * @param pageNo 页号
     * @param pageSize 页大小
     * @param projectId 项目id
     * @param entityName  检索名称
     * @param userId 当前登录用户id
     * @return {@link Pagination<ProjectMemberBO>} 成员的分页类
     */
    public Pagination<ProjectMemberBO> getProjectMembers(Integer pageNo,Integer pageSize,Long projectId, String entityName, Long userId){

        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        ResponseEntity<List<ProjectMemberEntity>> responseEntity = projectFeign.projectsProjectIdMembersGet(projectId,entityName,auth);
        List<ProjectMemberEntity> projectMemberEntities = responseEntity.getBody();
        Pagination<ProjectMemberBO> pagination = new Pagination<>();

        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        int count = 0 ;
        if (projectMemberEntities!=null){
            count = projectMemberEntities.size();
        }
        pagination.setTotalCount(count);

        if (count==0){
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }else {
            pagination.setPageList(BeanUtils.convertList(projectMemberEntities,ProjectMemberBO.class));
            return pagination;
        }

    }

    /**
     * 向项目中添加成员
     * @param projectId 项目id
     * @param userName 添加的用户名称
     * @param roleId 角色id
     * @param userId 目前登录用户id
     */
    public void addMember(Integer projectId,String userName,Integer roleId,Long userId){

        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userName);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setRoleId(roleId);
        projectMember.setMemberUser(userEntity);
        projectFeign.projectsProjectIdMembersPost(projectId,projectMember,auth);
    }

    /**
     * @param projectId 项目id
     * @param mid 成员id
     * @param userId 登录用户id
     */

    public void deleteMember(Integer projectId,Long mid,Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        projectFeign.projectsProjectIdMembersMidDelete(projectId,mid,auth);
    }

    /**
     *
     * @param projectId 项目id
     * @param mid 成员id
     * @param roleId 角色id
     * @param userId 登录用户id
     */
    public void putMember(Integer projectId,Long mid,Integer roleId,Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRoleId(roleId);
        projectFeign.projectsProjectIdMembersMidPut(projectId,mid,roleRequest,auth);
    }


}
