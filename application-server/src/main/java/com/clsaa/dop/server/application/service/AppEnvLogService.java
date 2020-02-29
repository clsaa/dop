package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppEnvLogRepository;
import com.clsaa.dop.server.application.model.bo.AppEnvBoV1;
import com.clsaa.dop.server.application.model.bo.KubeYamlDataBoV1;
import com.clsaa.dop.server.application.model.po.AppEnv;
import com.clsaa.dop.server.application.model.po.AppEnvLog;
import com.clsaa.dop.server.application.model.vo.AppEnvLogV1;
import com.clsaa.dop.server.application.model.vo.LogInfoV1;
import com.clsaa.rest.result.Pagination;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service(value = "AppEnvLogService")
public class AppEnvLogService {
    @Autowired
    private AppEnvService appEnvService;
    @Autowired
    private UserService userService;
    @Autowired
    private KubeYamlService kubeYamlService;
    @Autowired
    private AppEnvLogRepository appEnvLogRepository;
    @Autowired
    private BuildTagRunningIdMappingService buildTagRunningIdMappingService;


    public Pagination<AppEnvLogV1> getLogByAppEnvId(Long loginUser, Integer pageNo, Integer pageSize, Long appEnvId) {
        Pagination<AppEnvLogV1> pagination = new Pagination<>();

        Sort sort = new Sort(Sort.Direction.DESC, "rtime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        //List<LogInfoV1> logInfoV1List=this.pipelineService.findPipelineLogByEnvId(appEnvId);
        List<String> runningIdList = this.buildTagRunningIdMappingService.findRunningIdByAppEnvId(appEnvId);
        List<AppEnvLogV1> appEnvLogV1List = new ArrayList<>();
        Set userIdList = new HashSet();
        Map<Long, String> idNameMap = new HashMap<>();
        Page<AppEnvLog> appEnvLogPage = this.appEnvLogRepository.findAllByIdIn(pageable, runningIdList);
        List<AppEnvLog> appEnvLogList = appEnvLogPage.getContent();
        for (AppEnvLog appEnvLog : appEnvLogList) {
            //BeanUtils.convertType(appEnvLog, AppEnvLogV1.class);
            if (appEnvLog != null) {
                Long id = appEnvLog.getRuser();

                if (!userIdList.contains(id)) {
                    userIdList.add(id);
                    try {
                        String userName = this.userService.findUserNameById(id);
                        idNameMap.put(id, userName);
                    } catch (Exception e) {
                        System.out.print(e);
                        throw e;
                    }

                }


                String ruserName = idNameMap.get(id);

                AppEnvLogV1 appEnvLogV1 = AppEnvLogV1.builder()
                        .status(appEnvLog.getStatus())
                        .imageUrl(appEnvLog.getImageUrl())
                        .commitUrl(appEnvLog.getCommitUrl())
                        .appEnvLog(appEnvLog.getAppEnvLog())
                        .id(appEnvLog.getId())
                        .rtime(appEnvLog.getRtime())
                        .ruserName(ruserName)
                        .build();
                // String log = logInfoV1List.get(i).getLog();
                //String result =  log.matches("docker push [a-z]+.[a-z]+.[a-z]+.[a-z]+/([a-z]+)/([a-z]+):([0-9]+)");

                //appEnvLogV1.setRuserName(ruserName);
                appEnvLogV1List.add(appEnvLogV1);
            }
        }

        //List<String> runningIdList= this.buildTagRunningIdMappingService.findRunningIdByAppEnvId(appEnvId);
        //
        //for(String runningId:runningIdList) {
        //
        //
        //}


        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.setTotalCount(Long.valueOf(appEnvLogPage.getTotalElements()).intValue());
        pagination.setPageList(appEnvLogV1List);
        return pagination;
    }

    public String readFile(String filePath) throws Exception {

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(filePath);
        InputStream inputStream = resource.getInputStream();
        File ttfFile = new File(filePath);
        FileUtils.copyInputStreamToFile(inputStream, ttfFile);
        FileReader reader = new FileReader(ttfFile); // 建
        BufferedReader br = new BufferedReader(reader);
        String content = "";
        StringBuilder sb = new StringBuilder();

        while (content != null) {
            content = br.readLine();

            if (content == null) {
                break;
            }

            sb.append(content + '\n');
        }

        br.close();
        return sb.toString();
    }

    public void addLog(Long loginUser, LogInfoV1 logInfoV1, Long appEnvId) throws Exception {
        AppEnvBoV1 appEnvBoV1 = this.appEnvService.findEnvironmentDetailById(loginUser, appEnvId);
        String logTemplate = this.readFile("classpath:log-template.txt");
        logTemplate = logTemplate.replace("<ENV_ID>", appEnvBoV1.getId().toString());
        logTemplate = logTemplate.replace("<ENV_NAME>", appEnvBoV1.getTitle().toString());
        logTemplate = logTemplate.replace("<DEPLOYMENT_STRATEGY>", appEnvBoV1.getDeploymentStrategy().toString());
        logTemplate = logTemplate.replace("<ENV_LEVEL>", appEnvBoV1.getEnvironmentLevel().toString());

        if (appEnvBoV1.getDeploymentStrategy() == AppEnv.DeploymentStrategy.KUBERNETES) {
            KubeYamlDataBoV1 kubeYamlDataBoV1 = this.kubeYamlService.findYamlDataByEnvId(loginUser, appEnvBoV1.getId());
            if (kubeYamlDataBoV1 != null) {
                String yamlLogTemplate = this.readFile("classpath:yaml-log-template.txt");
                yamlLogTemplate = yamlLogTemplate.replace("<NAMESPACE>", kubeYamlDataBoV1.getNameSpace());
                yamlLogTemplate = yamlLogTemplate.replace("<SERVICE>", kubeYamlDataBoV1.getService());
                yamlLogTemplate = yamlLogTemplate.replace("<YAML>", kubeYamlDataBoV1.getDeploymentEditableYaml() == null ? kubeYamlDataBoV1.getYamlFilePath() : kubeYamlDataBoV1.getDeploymentEditableYaml());
                logTemplate = logTemplate + yamlLogTemplate;
            }
        }
        LocalDateTime now = LocalDateTime.now();
        AppEnvLog appEnvLog = AppEnvLog.builder()
                .commitUrl(logInfoV1.getCommitUrl())
                .imageUrl(logInfoV1.getImageUrl())
                .rtime(logInfoV1.getRtime())
                .ruser(logInfoV1.getRuser())
                .status(logInfoV1.getStatus())
                .is_deleted(false)
                .ctime(now)
                .mtime(now)
                .cuser(loginUser)
                .muser(loginUser)
                .id(logInfoV1.getRunningId())
                .appEnvLog(logTemplate)
                .build();

        appEnvLogRepository.saveAndFlush(appEnvLog);
    }
}
