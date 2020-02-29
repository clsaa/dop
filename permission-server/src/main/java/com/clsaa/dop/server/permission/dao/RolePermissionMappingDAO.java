package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.RolePermissionMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 角色功能点关联 DAO层，用来与数据库进行交互
 *
 * @author lzy
 *
 *
 * since :2019.3.11
 */
public interface RolePermissionMappingDAO extends JpaRepository<RolePermissionMapping, Long>
{
    /**
     * 根据角色ID和功能点ID查询关联关系
     *
     * @param roleId  角色ID
     * @param permissionId  功能点ID
     * @return {@link RolePermissionMapping}
     */
    RolePermissionMapping findByRoleIdAndPermissionId(Long roleId,Long permissionId);
    /**
     * 根据角色ID查询关联关系
     *
     * @param roleId  角色ID
     * @return {@link RolePermissionMapping}
     */
    List<RolePermissionMapping> findByRoleId(Long roleId);
    /**
     * 根据功能点ID查询关联关系
     *
     * @param permissionId  功能点ID
     * @return {@link RolePermissionMapping}
     */
    List<RolePermissionMapping> findByPermissionId(Long permissionId);

}