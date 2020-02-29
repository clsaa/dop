package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.ProjectFeign;
import com.clsaa.dop.server.image.model.bo.AccessLogBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.AccessLog;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.dop.server.image.util.TimeConvertUtil;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *     项目日志的业务实现
 * </p>
 * @author xzt
 * @since 2019-3-27
 */
@Service
public class ProjectLogsService {

    private final ProjectFeign projectFeign;
    private final UserFeign userFeign;

    @Autowired
    public ProjectLogsService(ProjectFeign projectFeign, UserFeign userFeign) {
        this.projectFeign = projectFeign;
        this.userFeign = userFeign;
    }

    /**
     * 通过参数来对项目的日志进行检索
     * @param projectId 项目id
     * @param userName 用户名
     * @param repo 仓库名称
     * @param tag 标签
     * @param operation 操作类型
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param page 页号
     * @param pageSize 页大小
     * @return {@link Pagination<AccessLogBO>}根据参数检索项目日志
     */
    public Pagination<AccessLogBO> getProjectLogs(Integer projectId, String userName, String repo, String tag,
                                                  String operation, String beginTime, String endTime, Integer page, Integer pageSize, Long userId){
        UserCredentialDto credentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(credentialDto);

        ResponseEntity<List<AccessLog>> responseEntity = projectFeign.projectsProjectIdLogsGet(projectId,userName,repo,tag,operation,beginTime,
                endTime,page,pageSize,auth);
        List<AccessLog> accessLogs = responseEntity.getBody();
        Pagination<AccessLogBO> pagination = new Pagination<>();



        List<String> httpHeader = responseEntity.getHeaders().get("X-Total-Count");


        int count = 0;
        if (!(httpHeader==null)){
            count = Integer.parseInt(httpHeader.get(0));
        }
        pagination.setTotalCount(count);
        pagination.setPageNo(page);
        pagination.setPageSize(pageSize);

        if (count==0){
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }else {
            List<AccessLogBO> accessLogBOS = new ArrayList<>();
            if (accessLogs!=null)
            for (AccessLog accessLog:accessLogs){
                AccessLogBO accessLogBO = BeanUtils.convertType(accessLog,AccessLogBO.class);
                accessLogBO.setOpTime(TimeConvertUtil.convertTime(accessLog.getOpTime()));
                accessLogBOS.add(accessLogBO);
            }
            pagination.setPageList(accessLogBOS);
            return pagination;
        }
    }

}
