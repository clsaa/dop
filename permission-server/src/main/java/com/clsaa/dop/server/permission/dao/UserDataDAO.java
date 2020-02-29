package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户数据表DAO层，用来与数据库进行交互
 *
 * @author lzy
 *
 *
 * since :2019.3.19
 */
public interface UserDataDAO extends JpaRepository<UserData, Long>{

    /**
     * 根据规则id查询数据列表
     *
     * @param ruleId  规则ID
     *
     * @return {@link List<UserData>}
     */
     List<UserData> findByRuleId(Long ruleId);

    /**
     * 根据用户ID和作用域参数值和规则ID查找数据
     *
     * @param userId  用户ID
     * @param fieldValue  作用域值
     * @param ruleId  规则ID
     *
     * @return {@link UserData}
     */
     UserData findByUserIdAndFieldValueAndRuleId(Long userId,Long fieldValue,Long ruleId);

    /**
     * 根据用户ID查找数据
     *
     * @param userId  用户ID
     * @param key 关键字
     * @return {@link List<UserData>}
     */
     List<UserData> findByUserIdAndDescriptionLike(Long userId,String key);

    /**
     * 根据规则id删除数据
     *
     * @param ruleId  规则ID
     *
     */
     void deleteByRuleId(Long ruleId);

    /**
     * 根据字段值查找用户ID列表
     *
     * @param fieldValue  作用域值
     * @param fieldName 作用域名称
     *
     * @return {List<Long>}
     */

    @Query(value = "select * from t_user_data d inner join t_user_rule r on d.rule_id=r.id"
            +" where d.field_value=:fieldValue and r.field_name=:fieldName and d.is_deleted = 0",nativeQuery = true)
    List<UserData> findUserByField(@Param("fieldValue") Long fieldValue,
                                   @Param("fieldName") String fieldName);

    /**
     * 根据作用域名称、值和用户ID删除用户数据
     *
     * @param fieldValue  作用域值
     * @param fieldName 作用域名称
     * @param userId 用户ID
     *
     * @return {List<Long>}
     */

    @Transactional
    @Modifying
    @Query(value = "update t_user_data d inner join t_user_rule r on d.rule_id=r.id set d.is_deleted = 1,d.user_id = uuid_short()"
            +" where d.field_value=:fieldValue and r.field_name=:fieldName and d.user_id=:userId and d.is_deleted = 0",
            nativeQuery = true)

    void deleteByFieldAndUserId(@Param("fieldValue") Long fieldValue,
                                          @Param("fieldName") String fieldName,
                                          @Param("userId") Long userId);

    /**
     * 得到某个功能点操作允许操作的数据范围
     *
     * @param permissionName  作用域值
     * @param fieldName 作用域名称
     * @param userId 用户ID
     *
     * @return {List<Long>}
     */

    @Query(value = "select * from t_user_data d inner join t_user_rule r on d.rule_id=r.id " +
            "inner join t_user_role_mapping ur on d.user_id=ur.user_id " +
            "inner join t_role_permission_mapping rp on ur.role_id=rp.role_id " +
            "inner join t_permission p on rp.permission_id=p.id "
            +"where p.name=:permissionName and d.user_id=:userId and r.field_name=:fieldName and " +
            "r.rule='in' and d.is_deleted = 0",nativeQuery = true)
    List<UserData> findAllIds(@Param("permissionName") String permissionName,
                              @Param("userId") Long userId,
                              @Param("fieldName") String fieldName);

    /**
     * 得到用户在某条数据上使用的角色ID
     *
     * @param fieldValue  作用域值
     * @param fieldName 作用域名称
     * @param userId 用户ID
     *
     * @return {Long}
     */

    @Query(value = "select role_id from t_user_rule ur inner join t_user_data ud on ud.rule_id=ur.id "
            +"where ud.field_value=:fieldValue and ud.user_id=:userId and ur.field_name=:fieldName and " +
            "ur.rule='in' and ud.is_deleted = 0",nativeQuery = true)
    List<Object> findRoleByUserAndRule(@Param("fieldValue") Long fieldValue,
                                         @Param("userId") Long userId,
                                         @Param("fieldName") String fieldName);


}
