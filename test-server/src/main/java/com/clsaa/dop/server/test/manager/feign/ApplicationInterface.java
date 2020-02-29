package com.clsaa.dop.server.test.manager.feign;

import com.clsaa.dop.server.test.config.FeignConfig;
import com.clsaa.dop.server.test.model.dto.Application;
import com.clsaa.dop.server.test.model.dto.Project;
import com.clsaa.rest.result.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xihao
 * @version 1.0
 * @since 08/05/2019
 */
@Component
@FeignClient(value = "application-server", configuration = FeignConfig.class)
public interface ApplicationInterface {

    @GetMapping("/pagedapp")
    Pagination<Application> findApplicationByProjectId(
            @RequestHeader("x-login-user") Long loginUser,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "projectId") Long projectId,
            @RequestParam(value = "queryKey", defaultValue = "") String queryKey);

    @GetMapping("/paged-project")
    Pagination<Project> findProjectOrderByCtimeWithPage(
            @RequestHeader("x-login-user") Long loginUser,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "includeFinished", defaultValue = "false") Boolean includeFinished,
            @RequestParam(value = "queryKey", defaultValue = "") String queryKey);

}
