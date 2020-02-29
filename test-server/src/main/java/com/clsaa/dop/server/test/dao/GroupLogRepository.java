package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.GroupExecuteLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xihao
 * @version 1.0
 * @since 08/05/2019
 */
public interface GroupLogRepository extends JpaRepository<GroupExecuteLog, Long> {

    Page<GroupExecuteLog> findByGroupIdOrderByCtimeDesc(Long groupId, Pageable pageable);

}
