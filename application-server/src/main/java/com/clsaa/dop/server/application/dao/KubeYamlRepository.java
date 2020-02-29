package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.KubeYamlData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KubeYamlRepository extends JpaRepository<KubeYamlData, Long> {

    /**
     * 根据应用环境id查询yaml数据
     *
     * @param appEnvId 应用环境id
     * @return {@link Optional< KubeYamlData >} 项目持久层对象
     */
    Optional<KubeYamlData> findByAppEnvId(Long appEnvId);

    Long countByAppEnvId(Long appEnvId);

}
