package com.clsaa.dop.server.pipeline.feign;

import com.clsaa.dop.server.pipeline.config.FeignConfig;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "pipeline-server", configuration = FeignConfig.class)
public interface PipelineFeign {
    /**
     * 创建jenkins
     * */
    @PostMapping("/v1/jenkins")
    void create(@RequestBody PipelineBoV1 pipelineBoV1);
    /**
     * 根据jenkinsfile创建jenkins
     * */
    @PostMapping("/v1/jenkins/jenkinsfile")
    void jenkinsfile(@RequestBody PipelineBoV1 pipelineBoV1);

    @ApiOperation(value = "运行流水线", notes = "根据流水线id查找开始运行流水线")
    @PostMapping("/v1/jenkins/build")
    void build(String pipelineId);
}
