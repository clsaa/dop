package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 30/03/2019
 */
public interface InterfaceCaseLogRepository extends JpaRepository<InterfaceExecuteLog, Long> {

    Page<InterfaceExecuteLog> findByCaseIdOrderByBeginDesc(Long caseId, Pageable pageable);
}
