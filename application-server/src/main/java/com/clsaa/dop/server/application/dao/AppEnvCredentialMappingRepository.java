package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.AppEnvCredentialMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppEnvCredentialMappingRepository extends JpaRepository<AppEnvCredentialMapping, Long> {
    /**
     * 根据appEnvId查询对应的environmentId
     *
     * @param appEnvId appEnvId
     * @return {@link  AppEnvCredentialMapping } 对应关系
     */
    Optional<AppEnvCredentialMapping> findByAppEnvId(Long appEnvId);
}
