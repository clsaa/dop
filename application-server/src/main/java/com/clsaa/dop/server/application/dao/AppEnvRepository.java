package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.AppEnv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppEnvRepository extends JpaRepository<AppEnv, Long> {


    /**
     * 根据projectId查询Application
     *
     * @param appId 应用Id
     * @return {@link  List< AppEnv > } 应用变量列表
     */
    List<AppEnv> findAllByAppId(Long appId);
}
