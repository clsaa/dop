package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.PermissionConfig;
import com.clsaa.dop.server.application.dao.ProjectRepository;
import com.clsaa.dop.server.application.model.bo.ProjectBoV1;
import com.clsaa.dop.server.application.model.po.Project;
import com.clsaa.dop.server.application.model.vo.ProjectV1;
import com.clsaa.dop.server.application.model.vo.UserV1;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service(value = "projectService")
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserService userService;
    @Autowired
    ImageService imageService;
    @Autowired
    private PermissionConfig permissionConfig;

    @Autowired
    private PermissionService permissionService;

    public void deleteMemberFromProject(Long userId, Long projectId, Long loginUser) {
        BizAssert.authorized(this.permissionService.check(permissionConfig.getDeleteMemberFromProject(), loginUser, permissionConfig.getProjectRuleFieldName(), projectId)
                , BizCodes.NO_PERMISSION);
        this.permissionService.deleteByFieldAndUserId(projectId, this.permissionConfig.getProjectRuleFieldName(), userId);
    }

    public void addMemberToProject(List<Long> userIdList, Long projectId, Long loginUser) {
        BizAssert.authorized(this.permissionService.check(permissionConfig.getAddMemberToProject(), loginUser, permissionConfig.getProjectRuleFieldName(), projectId)
                , BizCodes.NO_PERMISSION);
        List<Long> existUserIdList = this.permissionService.getProjectMembers(this.permissionConfig.getProjectRuleFieldName(), projectId);
        Set<Long> userIdSet = new HashSet<>(existUserIdList);

        for (Long userId : userIdList) {
            BizAssert.validParam(!existUserIdList.contains(userId), new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户" + String.valueOf(userId) + "已在项目中"));
            try {
                this.permissionService.addData(this.permissionConfig.getDeveloperAndProjectRuleId(), Long.valueOf(userId), projectId, loginUser);
            } catch (Exception e) {
                BizAssert.justFailed(new BizCode(BizCodes.INVALID_PARAM.getCode(), "用户" + String.valueOf(userId) + "添加失败，请检查该用户的角色"));
            }
            //this.permissionService.addRoleToUser(userId,this.permissionConfig.get);
        }

    }

    public List<UserV1> getMembersInProject(Long projectId) {
        List<Long> userIdList = this.permissionService.getProjectMembers(this.permissionConfig.getProjectRuleFieldName(), projectId);
        return userIdList.stream().map(l -> this.userService.findUserById(l)).collect(Collectors.toList());
    }

    /**
     * 分页查询项目
     *
     * @param pageNo          页号
     * @param pageSize        页大小
     * @param includeFinished 是否包含已结项目
     * @param queryKey        查询关键字
     * @return {@link Pagination<ProjectBoV1>}
     */
    public Pagination<ProjectV1> findProjectOrderByCtimeWithPage(Long loginUser, Integer pageNo, Integer pageSize, Boolean includeFinished, String queryKey) {

        //
        //BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewProject(), loginUser)
        //        , BizCodes.NO_PERMISSION);

        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        //得到可以查看的所有数据的ID列表
        List<Long> idList = permissionService.findAllIds(permissionConfig.getViewProject(), loginUser, permissionConfig.getProjectRuleFieldName());

        Page<Project> projectPage;
        List<Project> projectList;
        Integer totalCount;

        /*
         *如果有查询关键字 则调用可以带检索的查询
         */
        if (!queryKey.equals("")) {
            //如果包含已结项目则不带状态查询
            if (includeFinished) {
                projectList = projectRepository.findAllByTitleStartingWithAndIdIn(queryKey, pageable, idList).getContent();

                //这一句是为了获得所有条目的数量
                totalCount = projectRepository.countAllByTitleStartingWithAndIdIn(queryKey, idList);
            } else {
                projectList = projectRepository.findAllByStatusAndTitleStartingWithAndIdIn(Project.Status.NORMAL, queryKey, pageable, idList).getContent();
                totalCount = projectRepository.countAllByStatusAndTitleStartingWithAndIdIn(Project.Status.NORMAL, queryKey, idList);
            }
        }
        /*
         *如果没有，则调用全部查询
         */
        else {
            if (includeFinished) {

                projectList = projectRepository.findAllByIdIn(pageable, idList).getContent();
                totalCount = projectRepository.countAllByIdIn(idList);
            } else {
                projectList = projectRepository.findAllByStatusAndIdIn(Project.Status.NORMAL, pageable, idList).getContent();
                totalCount = projectRepository.countAllByStatusAndIdIn(Project.Status.NORMAL, idList);
            }
        }

        //projectList.stream().map(l -> BeanUtils.convertType(l, ProjectV1.class)).collect(Collectors.toList());
        List<ProjectV1> projectV1List = projectList.stream().map(l -> BeanUtils.convertType(l, ProjectV1.class)).collect(Collectors.toList());

        Set userIdList = new HashSet();
        Map<Long, String> idNameMap = new HashMap<>();
        for (int i = 0; i < projectV1List.size(); i++) {
            Long id = projectV1List.get(i).getCuser();

            if (!userIdList.contains(id)) {
                userIdList.add(id);
                try {
                    String userName = this.userService.findUserNameById(id);
                    idNameMap.put(id, userName);
                } catch (Exception e) {
                    System.out.print(e);
                    throw e;
                }

            }

            ProjectV1 projectV1 = projectV1List.get(i);
            projectV1.setCuserName(idNameMap.get(projectV1.getCuser()));
            projectV1List.set(i, projectV1);
        }


        //新建VO层对象 并赋值
        Pagination<ProjectV1> pagination = new Pagination<>();
        pagination.setTotalCount(totalCount);
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        if (projectList.size() == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }
        pagination.setPageList(projectV1List);

        return pagination;
    }

    public ProjectBoV1 findProjectById(Long loginUser, Long projectId) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewProject(), loginUser)
                , BizCodes.NO_PERMISSION);
        System.out.print(this.projectRepository.findById(projectId).orElse(null));
        return BeanUtils.convertType(this.projectRepository.findById(projectId).orElse(null), ProjectBoV1.class);
    }

    /**
     * 创建项目
     *
     * @param title       项目名称
     * @param description 项目描述
     */
    public void createProjects(Long loginUser, String title, Long origanizationId, String description, String status) {

        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getCreateProject(), loginUser)
                , BizCodes.NO_PERMISSION);


        LocalDateTime ctime = LocalDateTime.now().withNano(0);
        LocalDateTime mtime = LocalDateTime.now().withNano(0);

        Project project = Project.builder()
                .title(title)
                .description(description)
                .cuser(loginUser)
                .muser(loginUser)
                .is_deleted(false)
                .organizationId(origanizationId)
                .status(Project.Status.NORMAL)
                .privateStatus(Project.PrivateStatus.valueOf(status))
                .ctime(ctime)
                .mtime(mtime)
                .build();
        this.projectRepository.saveAndFlush(project);
        System.out.print(permissionConfig.getProjectManagerAndProjectRuleId());
        System.out.print(project.getId());
//         this.imageService.createProject(title, status, loginUser);
        this.permissionService.addData(permissionConfig.getProjectManagerAndProjectRuleId(), loginUser, project.getId(), loginUser);


    }
}
