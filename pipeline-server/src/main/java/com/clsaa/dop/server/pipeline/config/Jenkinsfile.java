package com.clsaa.dop.server.pipeline.config;


import com.clsaa.dop.server.pipeline.model.po.Stage;
import com.clsaa.dop.server.pipeline.model.po.Step;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import org.yaml.snakeyaml.Yaml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


/**
 * 生成jenkinsfile
 *
 * @author 张富利
 * @since 2019-03-09
 */

public class Jenkinsfile {

    private String stages;
    private Long appEnvId;
    private String git;
    private String dir;

    public Jenkinsfile() {
    }


    public Jenkinsfile(Long appEnvId, ArrayList<Stage> pipelineStage) {
        this.appEnvId = appEnvId;
        this.stages = "";

        for (int i = 0; i < pipelineStage.size(); i++) {
            Stage stage = pipelineStage.get(i);

            String name = stage.getName();
            this.stages += "stage(\'" + name + "\'){ \n";
            ArrayList<Step> steps = stage.getSteps();
            if (steps.size() == 0) {
                this.stages += "echo \'  没有运行的脚本 \' \n";
            }
            for (int j = 0; j < steps.size(); j++) {
                this.stages += "steps{\n";
                Step task = steps.get(j);
                Step.TaskType taskName = task.getTaskName();
                String gitUrl = task.getGitUrl();
                if(!task.getGitUrl().isEmpty()){
                    this.git = task.getGitUrl();
                    String folderGit = this.git.split("/")[this.git.split("/").length-1];
                    this.dir = folderGit.split("[.]")[0];
                }

                String dockerUserName = task.getDockerUserName();
                String respository = task.getRepository();
                String dockerPassword = task.getDockerPassword();
                String respositoryVersion = task.getRepositoryVersion();
                String shell = task.getShell();
                String deploy = task.getDeploy();
                String ip = task.getIp();
                String token = task.getToken();
                String dockerRepoHost = "registry.dop.clsaa.com";
                String dockerRepoPath = "/default";
                String imageName = dockerRepoHost + dockerRepoPath;
                try {
                    if (respository.startsWith("http")) {
                        dockerRepoHost = new URL(respository).getHost();
                        dockerRepoPath = new URL(respository).getPath();
                        imageName = dockerRepoHost + dockerRepoPath;
                    } else {
                        dockerRepoHost = respository.split("/")[0];
                        imageName = respository;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    BizAssert.justFailed(new BizCode(BizCodes.INVALID_PARAM.getCode()
                            , e.getMessage()));
                }
                switch (taskName) {
                    case PullCode:
                        this.stages += "deleteDir() \n";
//                        this.stages += "git \"" + gitUrl + "\" \n";
                        this.stages += "sh \'git clone \"" + gitUrl + "\" \'\n";
                        if(!this.dir.isEmpty()){
                            this.stages += "dir(\"" + this.dir + "\"){ \n";
                            this.stages += "sh \'echo commitId `git rev-parse HEAD`\' \n";
                            this.stages += "}\n";
                        }
                        break;
                    case BuildMaven:
                        if(!this.dir.isEmpty()){
                            this.stages += "dir(\"" + this.dir + "\"){ \n";
                            this.stages += "sh \'mvn --version \' \n";
                            this.stages += "sh \"mvn -U -am clean package \" \n";
                            this.stages += "}\n";
                        }else{
                            this.stages += "sh \'mvn --version \' \n";
                            this.stages += "sh \"mvn -U -am clean package \" \n";
                        }
                        break;
                    case BuildNode:
                        this.stages += "sh \'npm --version \' \n";
                        this.stages += "sh \'node --version \' \n";
//                        this.stages += "sh \'npm install \' \n";
                        break;
                    case BuildDjanggo:
//                        this.stages += "sh \'pip freeze > ./requirements.txt \' \n";
//                        this.stages += "sh \'pip install -r ./requirements.txt \' \n";
//                        this.stages += "sh \'python ./manage.py runserver \' \n";
                        this.stages += "sh \'python --version \' \n";
                        this.stages += "sh \'pip --version \' \n";
                        break;
                    case BuildDocker:
                        if(this.dir.isEmpty()){
                            this.stages += "sh \'docker build -t " + imageName + ":" + respositoryVersion + " ./\' \n";
                        }else{
                            this.stages += "dir(\"" + this.dir + "\"){ \n";
                            this.stages += "sh \'docker build -t " + imageName + ":" + respositoryVersion + " ./\' \n";
                            this.stages += "}\n";
                        }

                        break;
                    case PushDocker:
                        if(this.dir.isEmpty()){
                            this.stages += "sh \'docker login -u \"" + dockerUserName + "\" -p \"" + dockerPassword + "\" " + dockerRepoHost + "\' \n";
                            this.stages += "sh \'docker push " + imageName + ":" + respositoryVersion + "\' \n";
                        }else{
                            this.stages += "dir(\"" + this.dir + "\"){ \n";
                            this.stages += "sh \'docker login -u \"" + dockerUserName + "\" -p \"" + dockerPassword + "\" " + dockerRepoHost + "\' \n";
                            this.stages += "sh \'docker push " + imageName + ":" + respositoryVersion + "\' \n";
                            this.stages += "}\n";
                        }
                        break;
                    case CustomScript:
                        this.stages += "sh \'" + shell + "\' \n";
                    case Deploy:
                        String[] deploys = deploy.split("---\n");
                        for (int z = 0; z < deploys.length; z++) {
                            Yaml yaml = new Yaml();
                            if (deploys[z] == "") {
                                break;
                            }
                            Map map = yaml.load(deploys[z]);
                            Object apiVersion = map.get("apiVersion");
                            Object kind = map.get("kind");
                            Map metadata = (Map) map.get("metadata");
                            Object namespace = metadata.get("namespace");
                            Object deploymentName = metadata.get("name");
                            if (kind.toString().equals("Deployment")) {
                                // apiVersion: apps/v1beta1
                                this.stages += "sh \'\'\'\n" +
                                        "curl -X PUT -H \'Content-Type:application/yaml\' " +
                                        "-k -H \'Bearer " + token + "\' " +
                                        ip +
                                        "/apis/" + apiVersion +
                                        "/namespaces/" + namespace.toString() +
                                        "/" + kind.toString().toLowerCase() + "s/" + deploymentName.toString() + " " +
                                        "-d \'\n" +
                                        deploys[z].trim() + "\n" +
                                        "\'\n" +
                                        " \'\'\'" + "\n";
                                this.stages += "sh \'\'\'\n" +
                                        "curl -X POST -H \'Content-Type:application/yaml\' " +
                                        "-k -H \'Bearer " + token + "\' " +
                                        ip +
                                        "/apis/" + apiVersion +
                                        "/namespaces/" + namespace.toString() +
                                        "/" + kind.toString().toLowerCase() + "s " +
                                        "-d \'\n" +
                                        deploys[z].trim() + "\n" +
                                        "\'\n" +
                                        " \'\'\'" + "\n";
                            } else {
                                this.stages += "sh \'\'\'\n" +
                                        "curl -X PUT -H \'Content-Type:application/yaml\' " +
                                        "-k -H \'Bearer " + token + "\' " +
                                        ip +
                                        "/api/" + apiVersion +
                                        "/namespaces/" + namespace.toString() +
                                        "/" + kind.toString().toLowerCase() + "s/" + deploymentName.toString() + " " +
                                        "-d \'\n" +
                                        deploys[z].trim() + "\n" +
                                        "\'\n" +
                                        " \'\'\'" + "\n";
                                this.stages += "sh \'\'\'\n" +
                                        "curl -X POST -H \'Content-Type:application/yaml\' " +
                                        "-k -H \'Bearer " + token + "\' " +
                                        ip +
                                        "/api/" + apiVersion +
                                        "/namespaces/" + namespace.toString() +
                                        "/" + kind.toString().toLowerCase() + "s " +
                                        "-d \'\n" +
                                        deploys[z].trim() + "\n" +
                                        "\'\n" +
                                        " \'\'\'" + "\n";
                            }
                        }
                        break;
                }
                this.stages += "}\n";
            }
            this.stages += "}\n";
        }
    }

    public String getScript() {
        return ("pipeline {\n" +
                "    agent any\n" +
                "    stages {\n" +
                this.stages + "\n" +
                "    }\n" +
                "}\n");
    }


}
