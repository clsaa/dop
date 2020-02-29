package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.AppUrlInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUrlInfoRepository extends JpaRepository<AppUrlInfo, Long> {

    /**
     * 根据projectId查询Application
     *
     * @param appId appId
     * @return {@link <AppUrlInfo>} 项目持久层对象
     */
    AppUrlInfo findByAppId(Long appId);
}
