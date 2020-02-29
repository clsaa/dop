package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.model.vo.PipelineResultV1;
import com.clsaa.dop.server.application.service.PipelineService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * <p>
 * 流水线服务API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-5-16
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class PipelineController {
    @Autowired
    private PipelineService pipelineService;

    @ApiOperation(value = "查询流水线日志", notes = "查询流水线日志")
    @GetMapping("/pipelineLog/{runningId}")
    public PipelineResultV1 findPipelineLogByRunningId(
            @PathVariable(value = "runningId") String runningId) {
        return this.pipelineService.getPipelineResultByRunningId(runningId);

    }
}
