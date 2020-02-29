package com.clsaa.dop.server.pipeline.service;


import com.clsaa.dop.server.pipeline.dao.ResultOutputRepository;
import com.clsaa.dop.server.pipeline.feign.ApplicationFeign;
import com.clsaa.dop.server.pipeline.model.bo.PipelineBoV1;
import com.clsaa.dop.server.pipeline.model.dto.LogInfoV1;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ResultOutputService {

    @Autowired
    private ResultOutputRepository resultOutputRepository;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private JenkinsService jenkinsService;
    @Autowired
    private ApplicationFeign applicationFeign;

    /**
     * 运行时，创建一条运行时的快照, 返回此快照的runningid
     */
    public String create(String pipelineid) {
        ObjectId id = new ObjectId();
        ResultOutput resultOutput = ResultOutput.builder()
                .id(id)
                .pipelineId(pipelineid)
                .ctime(LocalDateTime.now())
                .result("")
                .status(ResultOutput.Status.RUNNING)
                .isDeleted(false).build();

        this.resultOutputRepository.insert(resultOutput);
        return id.toString();
    }

    public void setResult(String pipelineId, String output, Long loginUser) {
        List<ResultOutput> resultOutputs = this.resultOutputRepository.findByPipelineId(pipelineId);
        ResultOutput running = null;
        for (int i = 0; i < resultOutputs.size(); i++) {
            ResultOutput resultOutput = resultOutputs.get(i);
            if (resultOutput.getStatus() == ResultOutput.Status.RUNNING) {
                running = resultOutput;
            }
        }
        running.setStatus(ResultOutput.Status.FINISHED);
        running.setResult(output);
        this.resultOutputRepository.save(running);

        Pipeline pipeline = this.pipelineService.findById(pipelineId);

        String git = null;
        Pattern patternGit = Pattern.compile("http(s)*://\\S+.git");
        Matcher matcherGit = patternGit.matcher(output);
        if (matcherGit.find()) {
            git = matcherGit.group();
            git = git.split("\\.git")[0];
        }

        String commitId = null;
//        Pattern patternCommitId = Pattern.compile("git\\s+checkout\\s+-f\\s+[a-z0-9]+");
        Pattern patternCommitId = Pattern.compile("commitId\\s+\\S+");
        Matcher matcherCommitId = patternCommitId.matcher(output);
        if (matcherCommitId.find()) {
            commitId = matcherCommitId.group().split("\\s+")[1];
        }

        String repository= null;
        Pattern patternRepository = Pattern.compile("Successfully\\s+tagged(.*)+");
        Matcher matcherRepository = patternRepository.matcher(output);
        if (matcherRepository.find()) {
            repository = matcherRepository.group();
            repository = repository.split("\\s+")[2];
        }


        String status = this.jenkinsService.getBuildResult(pipelineId);
        LogInfoV1 logInfoV1 = LogInfoV1.builder()
                .runningId(running.getId().toString())
                .commitUrl(git + "/" + commitId)
                .imageUrl(repository)
                .rtime(running.getCtime())
                .ruser(loginUser)
                .Status(status)
                .build();
        this.applicationFeign.addLog(loginUser, pipeline.getAppEnvId(), logInfoV1);
    }

    public ResultOutput findByRunningId(String runningId) {
        Optional<ResultOutput> optionalResultOutput = this.resultOutputRepository.findById(new ObjectId(runningId));
        if (optionalResultOutput.isPresent()) {
            ResultOutput resultOutput = optionalResultOutput.get();
            return resultOutput;
        } else {
            return null;
        }
    }
}
