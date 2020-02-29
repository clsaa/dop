package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.App;
import com.clsaa.dop.server.application.model.po.AppVariable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppVarRepository extends JpaRepository<AppVariable, Long> {
    /**
     * 根据appId查询AppVariable
     *
     * @param appId 应用Id
     * @param sort  排序
     * @return {@link List <AppVariable>} 应用变量持久层对象
     */
    List<AppVariable> findAllByAppId(Long appId, Sort sort);

    /**
     * 根据appId 和 varKey 查询varValue
     *
     * @param appId  应用Id
     * @param varKey 键
     * @return {@link String} varValue
     */
    AppVariable findByAppIdAndVarKey(Long appId, String varKey);

}
