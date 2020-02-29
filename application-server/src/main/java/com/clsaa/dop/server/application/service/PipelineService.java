package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.PipelineFeign;
import com.clsaa.dop.server.application.model.vo.PipelineIdAndNameV1;
import com.clsaa.dop.server.application.model.vo.PipelineResultV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "PipelineService")
public class PipelineService {
    @Autowired
    private PipelineFeign pipelineFeign;

    public PipelineIdAndNameV1 findPipelineByAppEnvId(Long appEnvId) {
        return this.pipelineFeign.findPipelineByAppEnvId(appEnvId).get(0);
    }

    public PipelineResultV1 getPipelineResultByRunningId(String runningId) {
        PipelineResultV1 result = this.pipelineFeign.findByRunningId(runningId);
        return result;
    }

    //public List<LogInfoV1> findPipelineLogByEnvId(Long appEnvId){
    //    return this.pipelineFeign.findPipelineLogByEnvId(appEnvId);
    //}
}
