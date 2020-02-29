package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.BuildTagRunningIdMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuildTagRunningIdMappingRepository extends JpaRepository<BuildTagRunningIdMapping, Long> {
    /**
     * 根据运行Id查询对应关系
     *
     * @param runningId 运行Id
     * @param appEnvId  应用环境Id
     * @return {@link BuildTagRunningIdMapping} 版本号运行Id对应关系
     */
    Optional<BuildTagRunningIdMapping> findByRunningIdAndAppEnvId(String runningId, Long appEnvId);

    /**
     * 根据运行Id查询对应关系
     *
     * @param runningId 运行Id
     * @return {@link BuildTagRunningIdMapping} 版本号运行Id对应关系
     */
    Optional<BuildTagRunningIdMapping> findByRunningId(String runningId);

    /**
     * 根据环境id查询对应关系
     *
     * @param appEnvId 运行Id
     * @return {@link  List<BuildTagRunningIdMapping>} 版本号运行Id对应关系
     */
    List<BuildTagRunningIdMapping> findAllByAppEnvId(Long appEnvId);
}
