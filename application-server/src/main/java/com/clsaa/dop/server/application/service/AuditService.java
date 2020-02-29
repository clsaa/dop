package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.AuditFeign;
import com.clsaa.dop.server.application.model.vo.LogDtoV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(value = "AuditService")
public class AuditService {
    @Autowired
    private AuditFeign auditFeign;

    public void addAuditFeign(String serviceId, String functionId, String userId, Long otime, Map<String, Object> values) {
        LogDtoV1 logDtoV1 = new LogDtoV1();
        logDtoV1.setServiceId(serviceId);
        logDtoV1.setFunctionId(functionId);
        logDtoV1.setUserId(userId);
        logDtoV1.setOtime(otime);
        logDtoV1.setValues(values);
        this.auditFeign.addAuditLog(logDtoV1);
    }


}
