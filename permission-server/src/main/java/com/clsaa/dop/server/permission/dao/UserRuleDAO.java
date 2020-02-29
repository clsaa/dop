package com.clsaa.dop.server.permission.dao;

import com.clsaa.dop.server.permission.model.po.UserRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户数据规则表DAO层，用来与数据库进行交互
 *
 * @author lzy
 *
 *
 * since :2019.3.19
 */
public interface UserRuleDAO extends JpaRepository<UserRule, Long> {

    /**
     * 根据角色ID和作用域名称和规则名称查询规则
     *
     * @param roleId  角色ID
     * @param fieldName  作用域名称
     * @param rule  规则名称
     * @return {@link UserRule}
     */
    UserRule findByRoleIdAndFieldNameAndRule(Long roleId, String fieldName, String rule);

    /**
     * 根据角色ID查询规则
     *
     * @param roleId  角色ID
     * @return {@link List<UserRule>}
     */
    List<UserRule> findByRoleId(Long roleId);

    /**
     * 根据角色ID删除规则
     *
     * @param roleId  角色ID
     * @return
     */
    void deleteByRoleId(Long roleId);

    /**
     * 根据fieldName查询规则
     *
     * @param fieldName  作用域名称
     * @return
     */
    List<UserRule> findByFieldName(String fieldName);


}
