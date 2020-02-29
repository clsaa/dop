package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 功能点DAO层，用来与数据库交互
 *
 * @author lzy
 *
 *
 * since :2019.3.1
 */

public interface PermissionRepository extends JpaRepository<Permission, Long>
{
    /**
     * 根据name查询功能点
     *
     * @param name 功能点名称
     * @return {@link Permission}
     */
    Permission findByName(String name);

    /**
     * 根据关键字key过滤查询
     *
     * @param  key 关键字
     * @param {List <Long> idList}
     * @param rowOffset
     * @param pageSize
     * @return {@link List < Permission >}
     */
    @Query(value = "select * from t_permission where id in :idList and name like concat(:key,'%') and is_deleted = 0"+
            " limit :rowOffset,:pageSize",nativeQuery = true)
    List<Permission> findAllByNameLikeAndIdIn(@Param("key") String key,
                                              @Param("idList") List<Long> idList,
                                              @Param("rowOffset") Integer rowOffset,
                                              @Param("pageSize") Integer pageSize);

    /**
     * 查询全部
     *
     * @param  {List <Long> idList}
     * @return {@link List < Permission >}
     */
    @Query(value = "select * from t_permission p where id in :idList and is_deleted = 0",nativeQuery = true)
    List<Permission> findByIdIn(@Param("idList") List<Long> idList);

    /**
     * 分页查询全部
     *
     * @param  {List <Long> idList}
     * @param rowOffset
     * @param pageSize
     * @return {@link List < Permission >}
     */
    @Query(value = "select * from t_permission where id in :idList and is_deleted = 0"+
            " limit :rowOffset,:pageSize",nativeQuery = true)
    List<Permission> findByIdIn( @Param("idList") List<Long> idList,
                                 @Param("rowOffset") Integer rowOffset,
                                 @Param("pageSize") Integer pageSize);

    /**
     * 根据角色ID查询功能点
     *
     * @param  {Long roleId}
     *
     * @return {@link List < Permission >}
     */
    @Query(value = " select * from t_permission p inner join t_role_permission_mapping rp on p.id=rp.permission_id "+
    "where rp.role_id=:roleId and p.is_deleted = 0",nativeQuery=true)
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询功能点
     *
     * @param  {Long userId}
     *
     * @return {@link List < Permission >}
     */
    @Query(value = " select * from t_permission p inner join t_role_permission_mapping rp on p.id=rp.permission_id "+
            "inner join t_user_role_mapping ur on rp.role_id=ur.role_id "+
            "where ur.user_id=:userId and p.is_deleted = 0",nativeQuery=true)
    List<Permission> findByUserId(@Param("userId")Long userId);

    /**
     * 判断用户是否有此功能点
     *
     * @param  {Long userId}
     * @param  permissionName
     *
     * @return {boolean}
     */
    @Query(value = " select * from t_permission p inner join t_role_permission_mapping rp on p.id=rp.permission_id "+
            "inner join t_user_role_mapping ur on rp.role_id=ur.role_id "+
            "where ur.user_id=:userId and p.name=:permissionName and p.is_deleted = 0",nativeQuery=true)
    List<Permission> findByUserIdAndPermissionName(@Param("userId")Long userId,
                                    @Param("permissionName")String permissionName);

}