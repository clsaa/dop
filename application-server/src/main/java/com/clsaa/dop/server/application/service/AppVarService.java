package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.PermissionConfig;
import com.clsaa.dop.server.application.dao.AppVarRepository;
import com.clsaa.dop.server.application.model.bo.AppVarBoV1;
import com.clsaa.dop.server.application.model.po.AppVariable;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.dop.server.application.util.DESUtil;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "AppVarService")
public class AppVarService {
    @Autowired
    AppVarRepository appVarRepository;
    @Autowired
    private PermissionConfig permissionConfig;

    @Autowired
    private PermissionService permissionService;

    /**
     * 创建变量
     *
     * @param loginUser 创建者
     * @param appId 应用id
     * @param key   键
     * @param value 值
     */
    public void createAppVarByAppId(Long loginUser, Long appId, String key, String value) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getCreateVar(), loginUser)
                , BizCodes.NO_PERMISSION);
        AppVariable appVariable = AppVariable.builder()
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(loginUser)
                .muser(loginUser)
                .is_deleted(false)
                .appId(appId)
                .varKey(key)
                .varValue(this.EncryptValue(value))
                .build();
        this.appVarRepository.saveAndFlush(appVariable);
    }

    public String EncryptValue(String value) {
        return DESUtil.getEncryptString(value);
    }

    public String DecryptValue(String value) {
        return DESUtil.getDecryptString(value);
    }


    /**
     * 查找appId下所有变量
     *
     * @param appId 应用id
     * @return {@link List<AppVarBoV1>}
     */
    public List<AppVarBoV1> findAppVarByAppIdOrderByKey(Long loginUser, Long appId) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewVar(), loginUser)
                , BizCodes.NO_PERMISSION);
        Sort sort = new Sort(Sort.Direction.ASC, "varKey");
        return this.appVarRepository.findAllByAppId(appId, sort).stream().map(l -> BeanUtils.convertType(l, AppVarBoV1.class)).collect(Collectors.toList());
    }

    /**
     * 修改变量
     *
     * @param id    id
     * @param loginUser 修改者
     * @param value 值
     */
    public void updateAppVarById(Long id, Long loginUser, String value) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getEditVar(), loginUser)
                , BizCodes.NO_PERMISSION);
        AppVariable appVariable = this.appVarRepository.findById(id).orElse(null);
        appVariable.setMtime(LocalDateTime.now());
        appVariable.setMuser(loginUser);
        appVariable.setVarValue(EncryptValue(value));
        this.appVarRepository.saveAndFlush(appVariable);
    }

    public String findValueByAppIdAndKey(Long appId, String key) {
        return this.DecryptValue(this.appVarRepository.findByAppIdAndVarKey(appId, key).getVarValue());
    }


    /**
     * 删除变量
     *
     * @param id id
     */
    public void delteAppVarById(Long loginUser, Long id) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getDeleteVar(), loginUser)
                , BizCodes.NO_PERMISSION);
        appVarRepository.deleteById(id);
    }
}
