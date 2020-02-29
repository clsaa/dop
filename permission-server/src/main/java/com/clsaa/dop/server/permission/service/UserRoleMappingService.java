package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.UserRoleMappingDAO;
import com.clsaa.dop.server.permission.model.po.RolePermissionMapping;
import com.clsaa.dop.server.permission.model.po.UserRoleMapping;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 *  用户角色关联关系 的增删改查
 *
 * @author lzy
 *
 * @since 2019.3.15
 */
@Service
public class UserRoleMappingService {
    @Autowired
    //关联表DAO
    private UserRoleMappingDAO userRoleMappingDAO;

    @Autowired
    //角色service
    private RoleService roleService;

    /* *
     *
     *  * @param id 关联
     *  * @param userId 用户id
     *  * @param roleId 角色id
     *
     *  * @param ctime 创建时间
     *  * @param mtime 修改时间
     *  * @param cuser 创建人
     *  * @param muser 修改人
     *  * @param deleted 删除标记
     *
     * since :2019.3.15
     */

    //添加一个关联关系
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void addRoleToUser(Long userId,Long roleId,Long cuser,Long muser)
    {
        UserRoleMapping existUserRoleMapping=this.userRoleMappingDAO.
                findByUserIdAndRoleId(userId,roleId);
        BizAssert.allowed(existUserRoleMapping==null||existUserRoleMapping.isDeleted(), BizCodes.REPETITIVE_MAPPING);
        if(existUserRoleMapping!=null&&(!existUserRoleMapping.isDeleted()))
        {
            existUserRoleMapping.setDeleted(false);
        }
        else
        {
            UserRoleMapping userRoleMapping=UserRoleMapping.builder()
                    .userId(userId)
                    .roleId(roleId)
                    .cuser(cuser)
                    .muser(muser)
                    .ctime(LocalDateTime.now())
                    .mtime(LocalDateTime.now())
                    .deleted(false)
                    .build();
            userRoleMappingDAO.saveAndFlush(userRoleMapping);
        }

    }

    //根据角色ID查找关联关系
    public List<UserRoleMapping> findByRoleId(Long roleId)
    {
        List<UserRoleMapping> userRoleMappingList=userRoleMappingDAO.findByRoleId(roleId);
        return userRoleMappingList;
    }

    //根据用户ID查找关联关系
    public List<UserRoleMapping> findByUserId(Long userId)
    {
        List<UserRoleMapping> userRoleMappingList=userRoleMappingDAO.findByUserId(userId);
        return userRoleMappingList;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    //删除特定用户的特定角色关联关系
    public void delete(Long userId,Long roleId)
    {
        UserRoleMapping existUserRoleMapping=this.userRoleMappingDAO.
                findByUserIdAndRoleId(userId,roleId);
        userRoleMappingDAO.delete(existUserRoleMapping);
    }

    //根据功能点ID删除关联关系
    public void deleteByUserId(Long userId)
    {
        List<UserRoleMapping> userRoleMappingList=userRoleMappingDAO.findByUserId(userId);
        userRoleMappingList.forEach(userRoleMapping -> {
            userRoleMappingDAO.delete(userRoleMapping);
        });
    }
    //根据角色ID删除关联关系
    public void deleteByRoleId(Long roleId)
    {
        List<UserRoleMapping> UserRoleMappingList=userRoleMappingDAO.findByRoleId(roleId);
        UserRoleMappingList.forEach(userRoleMapping -> {
            userRoleMappingDAO.delete(userRoleMapping);
        });
    }

}
