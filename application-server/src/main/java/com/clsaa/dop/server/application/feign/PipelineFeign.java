package com.clsaa.dop.server.application.feign;

import com.clsaa.dop.server.application.config.FeignConfig;
import com.clsaa.dop.server.application.model.vo.PipelineIdAndNameV1;
import com.clsaa.dop.server.application.model.vo.PipelineResultV1;
import com.clsaa.dop.server.application.model.vo.UserNameV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "pipeline-server", configuration = FeignConfig.class)
public interface PipelineFeign {
    /**
     * 通过Id获取用户名称
     *
     * @return {@link UserNameV1}
     */
    @GetMapping("/v1/pipeline/envId/{envId}")
    List<PipelineIdAndNameV1> findPipelineByAppEnvId(@PathVariable("envId") Long envId);


    @GetMapping("/v1/resultOutput")
    PipelineResultV1 findByRunningId(@RequestParam(value = "runningId") String runningId);
    ///**
    // * 通过环境id获取日志列表
    // *
    // * @return {@link List<LogInfoV1>}
    // */
    //@GetMapping("/v1/pipeline/runningId/{runningId}")
    //List<LogInfoV1> findPipelineLogByEnvId(@PathVariable("envId") Long envId);
}
