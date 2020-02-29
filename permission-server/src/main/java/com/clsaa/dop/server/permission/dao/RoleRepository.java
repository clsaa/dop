package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 角色DAO层，用来与数据库进行交互
 *
 * @author lzy
 *
 *
 * since :2019.3.7
 */

public interface RoleRepository extends JpaRepository<Role, Long>
{
    /**
     * 根据name查询角色
     *
     * @param name 角色名称
     * @return {@link Role}
     */
    Role findByName(String name);

    /**
     * 查询全部
     *
     * @param  {List <Long> idList}
     * @param rowOffset
     * @param pageSize
     * @return {@link Page<Role>}
     */
    @Query(value = "select * from t_role where id in :idList and is_deleted = 0 "+
            "limit :rowOffset,:pageSize ",nativeQuery = true)
    List<Role> findByIdIn(@Param("idList") List<Long> idList,
                          @Param("rowOffset") Integer rowOffset,
                          @Param("pageSize") Integer pageSize);

    /**
     * 根据关键字key过滤查询
     *
     * @param  key 关键字
     * @param {List <Long> idList}
     * @param rowOffset
     * @param pageSize
     * @return {@link List < Permission >}
     */
    @Query(value = "select * from t_role where id in :idList and name like concat(:key,'%') and is_deleted = 0 "+
            "limit :rowOffset,:pageSize  ",nativeQuery = true)
    List<Role> findAllByNameLikeAndIdIn(@Param("key") String key,
                                        @Param("idList") List<Long> idList,
                                        @Param("rowOffset") Integer rowOffset,
                                        @Param("pageSize") Integer pageSize);

    /**
     * 查询全部
     *
     * @param  {List <Long> idList}
     * @return {@link List < Permission >}
     */
    @Query(value = "select * from t_role r where id in :idList and is_deleted = 0 ",nativeQuery = true)
    List<Role> findByIdIn(@Param("idList")List<Long> idList);


    /**
     * 根据功能点ID查询角色
     *
     * @param   permissionId
     * @return {@link List < Role >}
     */
    @Query(value = "select * from t_role r inner join t_role_permission_mapping rp on r.id=rp.role_id "+
            "where rp.permission_id=:permissionId and r.is_deleted = 0",nativeQuery = true)
    List<Role> findByPermissionId(@Param("permissionId")Long permissionId);

    /**
     * 根据用户ID查询角色
     *
     * @param   userId
     * @return {@link List < Role >}
     */
    @Query(value = "select * from t_role r inner join t_user_role_mapping ur on r.id=ur.role_id "+
            "where ur.user_id=:userId and r.is_deleted = 0",nativeQuery = true)
    List<Role> findByUserId(@Param("userId")Long userId);

}