package com.clsaa.dop.server.application.service;


import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.PermissionConfig;
import com.clsaa.dop.server.application.dao.AppRepository;
import com.clsaa.dop.server.application.model.bo.AppBoV1;
import com.clsaa.dop.server.application.model.bo.AppUrlInfoBoV1;
import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppEnv;
import com.clsaa.dop.server.application.model.po.AppUrlInfo;
import com.clsaa.dop.server.application.model.vo.AppBasicInfoV1;
import com.clsaa.dop.server.application.model.vo.AppV1;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "AppService")
public class AppService {
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private AppUrlInfoService appUrlInfoService;
    @Autowired
    private AppEnvService appEnvService;
    @Autowired
    private PermissionConfig permissionConfig;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;

    /**
     * 通过projectId分页查询应用
     *
     * @param pageNo    页号
     * @param pageSize  页大小
     * @param projectId 项目Id
     * @param queryKey  查询关键字
     * @return {@link Pagination< AppBoV1>}
     */
    public Pagination<AppV1> findApplicationByProjectIdOrderByCtimeWithPage(Long loginUser, Integer pageNo, Integer pageSize, Long projectId, String queryKey) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewApp(), loginUser)
                , BizCodes.NO_PERMISSION);

        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        //List<Long> idList = this.permissionService.findAllIds(this.permissionConfig.getViewApp(), loginUser, this.permissionConfig.getAppRuleFieldName());

        Page<App> applicationPage;
        List<App> applicationList;
        Integer totalCount;

        //如果带查询则使用上面的 带前缀查询的函数
        if (!queryKey.equals("")) {
            applicationPage = appRepository.findAllByProjectIdAndTitleStartingWith(projectId, queryKey, pageable);

            applicationList = applicationPage.getContent();
            totalCount = appRepository.countAllByProjectIdAndTitleStartingWith(projectId, queryKey);
        } else {

            applicationPage = appRepository.findAllByProjectId(projectId, pageable);

            applicationList = applicationPage.getContent();

            totalCount = appRepository.countAllByProjectId(projectId);
            //applicationList = applicationPage.getContent();

        }


        List<AppV1> appV1List = applicationList.stream().map(l -> BeanUtils.convertType(l, AppV1.class)).collect(Collectors.toList());


        Set userIdList = new HashSet();
        Map<Long, String> idNameMap = new HashMap<>();
        for (int i = 0; i < appV1List.size(); i++) {
            Long id = appV1List.get(i).getOuser();

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

            AppV1 appV1 = appV1List.get(i);
            appV1.setOuserName(idNameMap.get(appV1.getOuser()));
            appV1List.set(i, appV1);
        }


        //新建BO层对象 并赋值
        Pagination<AppV1> pagination = new Pagination<>();
        pagination.setTotalCount(totalCount);
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        if (applicationList.size() == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }
        pagination.setPageList(appV1List);

        return pagination;


    }

    /**
     * 删除应用
     *
     * @param id appId
     */
    public void deleteApp(Long loginUser, Long id) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getDeleteApp(), loginUser)
                , BizCodes.NO_PERMISSION);
        this.appRepository.deleteById(id);
        //appUrlInfoService.deleteAppUrlInfo(id);
        //AppBasicEnvironmentServer.deleteAppUrlInfo(String sId);
        //AppUrlInfoService.deleteAppUrlInfo(String sId);
    }

    /**
     * 根据拥有者查询应用
     *
     * @param ouser ouser
     */
    public List<AppBoV1> findApplicationByOuser(Long ouser){
        return this.appRepository.findAllByOuser(ouser).stream().map(l->BeanUtils.convertType(l,AppBoV1.class)).collect(Collectors.toList());
    }

    /**
     * 编辑应用
     *
     * @param id appId
     */
    public void updateApp(Long loginUser, Long id, String title, String description) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getEditApp(), loginUser)
                , BizCodes.NO_PERMISSION);
        App app = this.appRepository.findById(id).orElse(null);
        app.setTitle(title);
        app.setDescription(description);
        this.appRepository.saveAndFlush(app);
        //appUrlInfoService.deleteAppUrlInfo(id);
        //AppBasicEnvironmentServer.deleteAppUrlInfo(String sId);
        //AppUrlInfoService.deleteAppUrlInfo(String sId);
    }



    /**
     * 创建应用
     *
     * @param projectId   项目Id
     * @param title       应用名称
     * @param description 应用描述
     *
     */
    public void createApp(Long loginUser, Long projectId, String title, String description, String productMode, String gitUrl, String imageUrl) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getCreateApp(), loginUser)
                , BizCodes.NO_PERMISSION);
        LocalDateTime ctime = LocalDateTime.now().withNano(0);
        LocalDateTime mtime = LocalDateTime.now().withNano(0);
        App app = App.builder()
                .projectId(projectId)
                .title(title)
                .description(description)
                .cuser(loginUser)
                .muser(loginUser)
                .ouser(loginUser)
                .is_deleted(false)
                .ctime(ctime)
                .mtime(mtime)
                .productMode(App.ProductMode.valueOf(productMode))
                .build();
        this.appRepository.saveAndFlush(app);

        Long appId = app.getId();

        AppEnv appEnv = AppEnv.builder()
                .appId(appId)
                .ctime(ctime)
                .cuser(loginUser)
                .is_deleted(false)
                .mtime(mtime)
                .muser(loginUser)
                .deploymentStrategy(AppEnv.DeploymentStrategy.KUBERNETES)
                .title("日常开发")
                .environmentLevel(AppEnv.EnvironmentLevel.DAILY)
                .build();
        this.appEnvService.createAppEnv(appEnv);

        AppUrlInfo appUrlInfo = AppUrlInfo.builder()
                .appId(appId)
                .ctime(ctime)
                .cuser(loginUser)
                .is_deleted(false)
                .imageUrl(imageUrl)
                .mtime(mtime)
                .muser(loginUser)
                .warehouseUrl(gitUrl)
                .build();
        this.appUrlInfoService.createAppUrlInfo(appUrlInfo);
        //this.permissionService.addData(permissionConfig.getProjectManagerAndAppRuleId(), loginUser, app.getId(), loginUser);

    }

    /**
     * 查询应用
     *
     * @param id id
     * @return @return {@link AppBasicInfoV1}
     */
    public AppBasicInfoV1 findAppById(Long loginUser, Long id) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewApp(), loginUser)
                , BizCodes.NO_PERMISSION);
        //System.out.print(this.appRepository.findById(id).orElse(null));
        AppBoV1 app = BeanUtils.convertType(this.appRepository.findById(id).orElse(null), AppBoV1.class);
        AppUrlInfoBoV1 appUrlInfoBoV1 = this.appUrlInfoService.findAppUrlInfoByAppId(id);
        AppBasicInfoV1 appBasicInfoV1 = AppBasicInfoV1.builder()
                .ctime(app.getCtime())
                .title(app.getTitle())
                .description(app.getDescription())
                .ouser(app.getOuser())
                .warehouseUrl(appUrlInfoBoV1.getWarehouseUrl())
                .productionDbUrl(appUrlInfoBoV1.getProductionDbUrl())
                .testDbUrl(appUrlInfoBoV1.getTestDbUrl())
                .productionDomain(appUrlInfoBoV1.getProductionDomain())
                .testDomain(appUrlInfoBoV1.getTestDomain())
                .imageUrl(appUrlInfoBoV1.getImageUrl())
                .build();
        return appBasicInfoV1;

    }
}
