package com.clsaa.dop.server.application.feign;

import com.clsaa.dop.server.application.config.FeignConfig;
import com.clsaa.dop.server.application.model.vo.LogDtoV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "audit-server", configuration = FeignConfig.class)

public interface AuditFeign {


    /**
     * 添加审计日志
     */
    @PostMapping("/v1/logs")
    boolean addAuditLog(@RequestBody LogDtoV1 logDtoV1);

}
