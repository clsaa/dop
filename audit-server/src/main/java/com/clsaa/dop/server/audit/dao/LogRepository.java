package com.clsaa.dop.server.audit.dao;

import com.clsaa.dop.server.audit.model.po.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 操作日志持久层
 *
 * @author joyren
 */
public interface LogRepository extends MongoRepository<Log, String> {
    /**
     * 查询某个人使用进行某个操作的日志
     *
     * @param serviceId
     * @param functionId
     * @param userId
     * @return
     */
    Page<Log> findLogsByServiceIdAndFunctionIdAndUserId(String serviceId, String functionId, String userId, Pageable pageable);
}
