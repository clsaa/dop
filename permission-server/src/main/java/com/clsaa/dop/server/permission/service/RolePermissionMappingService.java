package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.RolePermissionMappingDAO;
import com.clsaa.dop.server.permission.model.po.RolePermissionMapping;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 *  角色功能点关联关系 的增删改查
 *
 * @author lzy
 *
 * @since 2019.3.9
 */
@Service
public class RolePermissionMappingService {
    @Autowired
    //关联表DAO
    private RolePermissionMappingDAO rolePermissionMappingDAO;
    @Autowired
    //功能点service
    private PermissionService permissionService;
    @Autowired
    //角色service
    private RoleService roleService;

    /* *
     *
     *  * @param id 关联
     *  * @param permissionId 功能点id
     *  * @param roleId 角色id
     *
     *  * @param ctime 创建时间
     *  * @param mtime 修改时间
     *  * @param cuser 创建人
     *  * @param muser 修改人
     *  * @param deleted 删除标记
     *
     * since :2019.3.11
     */

    //添加一个关联关系
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void addPermissionToRole(Long roleId,Long permissionId,Long cuser,Long muser)
    {
        RolePermissionMapping existRolePermissionMapping=this.rolePermissionMappingDAO.
                findByRoleIdAndPermissionId(roleId,permissionId);
        BizAssert.allowed(existRolePermissionMapping==null, BizCodes.REPETITIVE_MAPPING);

            RolePermissionMapping rolePermissionMapping=RolePermissionMapping.builder()
                    .roleId(roleId)
                    .permissionId(permissionId)
                    .cuser(cuser)
                    .muser(muser)
                    .ctime(LocalDateTime.now())
                    .mtime(LocalDateTime.now())
                    .deleted(false)
                    .build();
            rolePermissionMappingDAO.saveAndFlush(rolePermissionMapping);


    }

    //根据角色ID查找关联关系
    public List<RolePermissionMapping> findByRoleId(Long roleId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingDAO.findByRoleId(roleId);
        return rolePermissionMappingList;
    }

    //根据功能点ID查找关联关系
    public List<RolePermissionMapping> findByPermissionId(Long permissionId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingDAO.findByPermissionId(permissionId);
        return rolePermissionMappingList;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    //删除特定角色特定功能点关联关系
    public void delete(Long roleId,Long permissionId)
    {
        RolePermissionMapping existRolePermissionMapping=this.rolePermissionMappingDAO.
                findByRoleIdAndPermissionId(roleId,permissionId);
        rolePermissionMappingDAO.delete(existRolePermissionMapping);
    }

    //根据功能点ID删除关联关系
    public void deleteByPermissionId(Long permissionId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingDAO.findByPermissionId(permissionId);
        rolePermissionMappingList.forEach(rolePermissionMapping -> {
            rolePermissionMappingDAO.delete(rolePermissionMapping);
        });
    }
    //根据角色ID删除关联关系
    public void deleteByRoleId(Long roleId)
    {
        List<RolePermissionMapping> rolePermissionMappingList=rolePermissionMappingDAO.findByRoleId(roleId);
        rolePermissionMappingList.forEach(rolePermissionMapping -> {
            rolePermissionMappingDAO.delete(rolePermissionMapping);
        });
    }

    //根据角色ID和功能点ID查找关联关系
    public RolePermissionMapping findByRoleIdAndPermissionId(Long roleId,Long permissionId)
    {
       return rolePermissionMappingDAO.findByRoleIdAndPermissionId(roleId,permissionId);
    }
}
