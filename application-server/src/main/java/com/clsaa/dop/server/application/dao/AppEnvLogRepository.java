package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.AppEnvLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface AppEnvLogRepository extends JpaRepository<AppEnvLog, String> {
    /**
     * 根据appEnvId查询对应的environmentId
     *
     * @param  c
     * @return {@link   Optional<AppEnvLog> } 对应关系
     */
    Page<AppEnvLog> findAllByIdIn(Pageable pageable, Collection<String> c);
}
