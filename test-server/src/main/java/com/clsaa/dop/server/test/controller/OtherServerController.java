package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.manager.feign.ApplicationInterface;
import com.clsaa.dop.server.test.model.dto.Application;
import com.clsaa.dop.server.test.model.dto.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 09/05/2019
 */
@RestController
@CrossOrigin
@RequestMapping("/other")
public class OtherServerController {

    @Autowired
    private ApplicationInterface applicationInterface;

    private Long userIdForInfo = 24L;

    @GetMapping("/projects")
    public List<Project> getAllProjects(@RequestParam("queryKey") String queryKey) {
        int pageSize = Integer.MAX_VALUE;
        int pageNo = 1;
        return applicationInterface.findProjectOrderByCtimeWithPage(userIdForInfo, pageNo, pageSize, true, queryKey).
                getPageList();
    }

    @GetMapping("/apps")
    public List<Application> getAllApp(@RequestParam("projectId") Long projectId, @RequestParam("queryKey") String queryKey) {
        int pageSize = Integer.MAX_VALUE;
        int pageNo = 1;
        return applicationInterface.findApplicationByProjectId(userIdForInfo, pageNo, pageSize, projectId, queryKey).getPageList();
    }
}
