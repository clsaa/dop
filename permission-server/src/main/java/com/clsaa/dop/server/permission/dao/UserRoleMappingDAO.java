package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.RolePermissionMapping;
import com.clsaa.dop.server.permission.model.po.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户角色关联 DAO层，用来与数据库进行交互
 *
 * @author lzy
 *
 *
 * since :2019.3.15
 */
public interface UserRoleMappingDAO extends JpaRepository<UserRoleMapping, Long>
{
    /**
     * 根据角色ID和用户ID查询关联关系
     *
     * @param userId  用户ID
     * @param roleId  角色ID
     *
     * @return {@link UserRoleMapping}
     */
    UserRoleMapping findByUserIdAndRoleId(Long userId,Long roleId);
    /**
     * 根据角色ID查询关联关系
     *
     * @param roleId  角色ID
     * @return {@link UserRoleMapping}
     */
    List<UserRoleMapping> findByRoleId(Long roleId);
    /**
     * 根据用户ID查询关联关系
     *
     * @param userId  用户ID
     * @return {@link  List<UserRoleMapping>}
     */
    List<UserRoleMapping> findByUserId(Long userId);

}