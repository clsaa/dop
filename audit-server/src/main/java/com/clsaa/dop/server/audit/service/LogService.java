package com.clsaa.dop.server.audit.service;

import com.clsaa.dop.server.audit.dao.LogRepository;
import com.clsaa.dop.server.audit.model.po.Log;
import com.clsaa.dop.server.audit.model.vo.LogV1;
import com.clsaa.dop.server.audit.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 操作日志业务实现类
 *
 * @author joyren
 */
@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 添加操作日志
     *
     * @param serviceId  服务id
     * @param functionId 功能id
     * @param userId     用户id
     * @param values     值
     * @param otime      业务发生时间
     */
    public void addLog(String serviceId, String functionId, String userId, Map<String, Object> values, Long otime) {
        Log log = Log.builder().serviceId(serviceId)
                .functionId(functionId)
                .userId(userId)
                .values(values)
                .otime(otime)
                .ctime(LocalDateTime.now())
                .build();
        this.logRepository.save(log);
    }

    /**
     * 分页查询操作日志，用户id为空字符串，则查询此功能全部用户的操作日志
     *
     * @param serviceId  服务id
     * @param functionId 功能id
     * @param userId     用户id
     * @param pageNo     页号
     * @param pageSize   页大小
     * @return {@link Pagination< LogV1>}
     */
    public Pagination<LogV1> findLog(String serviceId, String functionId, String userId, Integer pageNo, Integer pageSize) {
        //构造查询条件
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("serviceId").is(serviceId)
                .and("functionId").is(functionId);
        if (StringUtils.isNotEmpty(userId)) {
            criteria.and("userId").is(userId);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "otime");
        query.addCriteria(criteria);
        query.with(sort);

        //查询数据总量
        int count = (int) this.mongoTemplate.count(query, Log.class);

        Pagination<LogV1> pagination = new Pagination<>();
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.setTotalCount(count);
        if (count == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }

        //查询实际数据
        Pageable pageRequest = PageRequest.of(pagination.getPageNo() - 1, pagination.getPageSize(), sort);
        query.with(pageRequest);
        List<Log> logs = this.mongoTemplate.find(query, Log.class);
        pagination.setPageList(logs.stream().map(l -> BeanUtils.convertType(l, LogV1.class)).collect(Collectors.toList()));
        return pagination;
    }
}
