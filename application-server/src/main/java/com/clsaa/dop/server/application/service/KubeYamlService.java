package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.KubernetesDefaultConfig;
import com.clsaa.dop.server.application.config.PermissionConfig;
import com.clsaa.dop.server.application.dao.KubeYamlRepository;
import com.clsaa.dop.server.application.model.bo.KubeCredentialBoV1;
import com.clsaa.dop.server.application.model.bo.KubeYamlDataBoV1;
import com.clsaa.dop.server.application.model.po.KubeYamlData;
import com.clsaa.dop.server.application.util.BeanUtils;
import com.clsaa.rest.result.bizassert.BizAssert;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1beta1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service(value = "KubeYamlService")
public class KubeYamlService {
    @Autowired
    KubeYamlRepository kubeYamlRepository;
    @Autowired
    KubernetesDefaultConfig kubernetesDefaultConfig;
    @Autowired
    AppEnvService appEnvService;
    @Autowired
    KubeCredentialService kubeCredentialService;
    @Autowired
    BuildTagRunningIdMappingService buildTagRunningIdMappingService;
    @Autowired
    AppUrlInfoService appUrlInfoService;
    @Autowired
    private PermissionConfig permissionConfig;

    @Autowired
    private PermissionService permissionService;

    public String readFile(String filePath) throws Exception {
        //File file = ResourceUtils.get(filePath);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(filePath);
        InputStream inputStream = resource.getInputStream();
        File ttfFile = new File(filePath);
        FileUtils.copyInputStreamToFile(inputStream, ttfFile);
        FileReader reader = new FileReader(ttfFile); // 建立一个输入流对象reader

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

    //public String test(Long appEnvId) throws Exception{
    //    String apiServer="/apis/networking.istio.io/v1alpha3/namespaces/";
    //    KubeCredentialBoV1 k = this.kubeCredentialService.queryByAppEnvId(appEnvId);
    //    String token = k.getTargetClusterToken();
    //    String url="http://open.dop.clsaa.com:6443";
    //    String nameSpace= this.kubeYamlRepository.findByAppEnvId(appEnvId).orElse(null).getNameSpace();
    //    String link = url+apiServer+nameSpace+"/virtualservices";
    //    //'Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC10b2tlbi1sY25kOCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImIyZDBlYTQzLTA5MzAtMTFlOS1hYmM3LTAwMTYzZTBlYzFjZiIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWRhc2hib2FyZCJ9.KlrkaUDeoyWngUwbmGS2C7gpSixEYJYRgv52w9v_YVLe_uDO_SdHAaQanxG8W23RbKxYPRt_0S7haFy-gU5ngbuYPxHVvPMoB8gVrPX8dGOvYpxvs26eOEjibgnfJTmegWBgylSP9ULKqLTgJ3feFiUyMtd_metvaCSJInPDonDFlvNTzLIn8sOxE3Qxq3fAApNgkxNeuHT8vygznoLysv0I3Tzobhn5R78q5D1QL01AxRlAIKm57i6h5X7utoXrnt8JbuLlMk2ZERa8ANTlhTDhFOj4ODiAqWgN2gtDUmX9ACGHr7kbU8HW_COj4QMS6gLNdnI4bBxTCWVSL-er9Q'  -d
    //
    //
    //    //String url = "http://www.google.com/search?q=mkyong";
    //
    //    URL obj = new URL(link);
    //    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    //
    //    //默认值我GET
    //    con.setRequestMethod("GET");
    //
    //    //添加请求头
    //    con.setRequestProperty("User-Agent", USER_AGENT);
    //    con.setRequestProperty("Bearer",token);
    //
    //
    //    int responseCode = con.getResponseCode();
    //    System.out.println("\nSending 'GET' request to URL : " + url);
    //    System.out.println("Response Code : " + responseCode);
    //
    //    BufferedReader in = new BufferedReader(
    //            new InputStreamReader(con.getInputStream()));
    //    String inputLine;
    //    StringBuffer response = new StringBuffer();
    //
    //    while ((inputLine = in.readLine()) != null) {
    //        response.append(inputLine);
    //    }
    //    in.close();
    //
    //    //打印结果
    //    System.out.println(response.toString());
    //    return  "xixi";
    //}
    public String createYamlFileForDeploy(Long loginUser, Long appEnvId, String runningId) throws Exception {


        KubeYamlDataBoV1 kubeYamlDataBoV1 = this.findYamlDataByEnvId(loginUser, appEnvId);
        if (kubeYamlDataBoV1.getYamlFilePath().equals("")) {
            String yaml = (kubeYamlDataBoV1.getDeploymentEditableYaml());
            String buildTag = buildTagRunningIdMappingService.findBuildTagByRunningIdAndAppEnvId(loginUser, runningId, appEnvId);
            Long appId = this.appEnvService.findAppIdById(appEnvId);
            String imageUrl = appUrlInfoService.findAppUrlInfoByAppId(appId).getImageUrl();
            yaml = yaml.replace("<IMAGE_URL>", imageUrl + ":" + buildTag);
            return yaml;

        } else {
            String path = kubeYamlDataBoV1.getYamlFilePath();
            String finalPath = "";
            if (path.matches("giltab")) {
                finalPath = path.replace("blob", "raw");
            } else {
                if (path.matches("github")) {
                    String[] splitPath = path.split("blob/");
                    finalPath = "https://raw.githubusercontent.com/" + splitPath[0].split("github.com/")[1] + splitPath[1];
                }
            }

            RestTemplate restTemplate = new RestTemplate();
            String yaml = restTemplate.getForObject(finalPath, String.class);
            String buildTag = buildTagRunningIdMappingService.findBuildTagByRunningIdAndAppEnvId(loginUser, runningId, appEnvId);
            yaml = yaml.replace("<IMAGE_URL>", buildTag);
            return yaml;
        }
    }


    /**
     * 创建YAML信息
     *
     * @param appEnvId        应用环境id
     * @param loginUser           创建者
     * @param nameSpace       命名空间
     * @param service         服务
     * @param deployment      部署
     * @param container       容器
     * @param releaseStrategy 发布策略
     * @param releaseBatch    发布批次
     * @param replicas        副本数量
     */
    public void CreateYamlData(Long appEnvId, Long loginUser, String nameSpace, String service, String deployment, String container, String releaseStrategy, Integer replicas
            , Long releaseBatch, String yamlFilePath) throws Exception {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getCreateYamlData(), loginUser)
                , BizCodes.NO_PERMISSION);
        KubeYamlData kubeYamlData = KubeYamlData.builder()
                .appEnvId(appEnvId)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(loginUser)
                .muser(loginUser)
                .is_deleted(false)
                .nameSpace(nameSpace)
                .service(service)
                .deployment(deployment)
                .containers(container)
                .replicas(replicas)
                .releaseBatch(releaseBatch)
                .releaseStrategy(KubeYamlData.ReleaseStrategy.valueOf(releaseStrategy))
                .build();


        generateYaml(appEnvId, nameSpace, service, deployment, container, releaseStrategy, replicas
                , releaseBatch, yamlFilePath, kubeYamlData);
    }


    /**
     * 更新YAML信息
     *
     * @param appEnvId        应用环境id
     * @param loginUser           创建者
     * @param nameSpace       命名空间
     * @param service         服务
     * @param deployment      部署
     * @param container       容器
     * @param releaseStrategy 发布策略
     * @param releaseBatch    发布批次
     * @param replicas        副本数量
     */
    public void updateYamlData(Long appEnvId, Long loginUser, String nameSpace, String service, String deployment, String container, String releaseStrategy, Integer replicas
            , Long releaseBatch, String yamlFilePath) throws Exception {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getEditYamlData(), loginUser)
                , BizCodes.NO_PERMISSION);
        KubeYamlData kubeYamlData = this.kubeYamlRepository.findByAppEnvId(appEnvId).orElse(null);
        kubeYamlData.setMtime(LocalDateTime.now());
        kubeYamlData.setMuser(loginUser);
        kubeYamlData.setNameSpace(nameSpace);
        kubeYamlData.setService(service);
        kubeYamlData.setDeployment(deployment);
        kubeYamlData.setContainers(container);
        kubeYamlData.setReplicas(replicas);
        kubeYamlData.setReleaseBatch(releaseBatch);
        //kubeYamlData.setImageUrl(imageUrl);

        kubeYamlData.setReleaseStrategy(KubeYamlData.ReleaseStrategy.valueOf(releaseStrategy));

        generateYaml(appEnvId, nameSpace, service, deployment, container, releaseStrategy, replicas
                , releaseBatch, yamlFilePath, kubeYamlData);


    }

    public void generateYaml(Long appEnvId, String nameSpace, String service, String deployment, String container, String releaseStrategy, Integer replicas
            , Long releaseBatch, String yamlFilePath, KubeYamlData kubeYamlData) throws Exception {
        if (yamlFilePath.equals("")) {
            long cur = new Date().getTime();
            int random = new Random().nextInt(1000000);
            String versionId = Long.toHexString(Long.valueOf(cur + random));
            CoreV1Api coreV1Api = getCoreV1Api(appEnvId);
            List<V1Service> serviceList = coreV1Api.listNamespacedService(nameSpace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            IntOrString targetPort = serviceList.get(0).getSpec().getPorts().get(0).getTargetPort();
            AppsV1beta1Api appsV1beta1Api = getAppsV1beta1Api(appEnvId);
            List<AppsV1beta1Deployment> deploymentList = appsV1beta1Api.listNamespacedDeployment(nameSpace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false).getItems();
            if (deploymentList.size() != 0) {
                for (int i = 0; i < deploymentList.size(); i++) {
                    AppsV1beta1Deployment AppsV1beta1Deployment = deploymentList.get(i);
                    if (AppsV1beta1Deployment.getMetadata().getName().equals(deployment) || deployment.equals("")) {
                        //    List<V1Container> containerList = AppsV1beta1Deployment.getSpec().getTemplate().getSpec().getContainers();
                        //    for (int j = 0; i < containerList.size(); j++) {
                        //        if (containerList.get(i).getName().equals(container) || container.equals((""))) {
                        //            V1Container container1 =containerList.get(i);
                        //            container1.setImage("<image_url>");
                        //            containerList.set(i,container1);
                        //
                        //            break;
                        //        }
                        //    }
                        //    V1LabelSelector selector = AppsV1beta1Deployment.getSpec().getSelector();
                        //    selector.getMatchLabels().put(kubernetesDefaultConfig.getVirtualServiceVersionLabel(),versionId);
                        //    AppsV1beta1Deployment.getSpec().getTemplate().getSpec().setContainers(containerList);
                        String apiVersion = AppsV1beta1Deployment.getApiVersion();
                        if (apiVersion == null) {
                            apiVersion = kubernetesDefaultConfig.getDeploymentApiVersion();
                        }
                        String kind = AppsV1beta1Deployment.getKind();
                        if (kind == null) {
                            kind = kubernetesDefaultConfig.getDeploymentKind();
                        }
                        V1ObjectMeta meta = AppsV1beta1Deployment.getMetadata();
                        V1ObjectMeta newMeta = new V1ObjectMetaBuilder()
                                .withLabels(meta.getLabels())
                                .withName(meta.getName())
                                .withNamespace(meta.getNamespace())
                                .build();
                        AppsV1beta1DeploymentSpec spec = AppsV1beta1Deployment.getSpec();

                        AppsV1beta1Deployment appsV1beta1Deployment = new AppsV1beta1DeploymentBuilder()
                                .withApiVersion(apiVersion)
                                .withKind(kind)
                                .withMetadata(newMeta)
                                .withSpec(spec)
                                .build();
                        String deploymentYaml = Yaml.dump(appsV1beta1Deployment);
                        deploymentYaml.replace("matchLabels:", "matchLabels:\n    version: <VERSION>");
                        deploymentYaml.replace("- image: .+$\n", "- image: <IMAGE_URL>\n");
                        kubeYamlData.setDeploymentEditableYaml(deploymentYaml);
                        //kubeYamlData.setDeploymentEditableYaml(Yaml.dump(AppsV1beta1Deployment));
                        break;
                    }
                }

            } else {
                String deploymentTemplate = this.readFile("classpath:deployment-template.yaml");
                deploymentTemplate = deploymentTemplate.replace("<NAME>", service);
                deploymentTemplate = deploymentTemplate.replace("<NAMESPACE>", nameSpace);
                deploymentTemplate = deploymentTemplate.replace("<CONTAINER_PORT>", String.valueOf(targetPort.getIntValue()));
                deploymentTemplate = deploymentTemplate.replace("<REPLICAS>", replicas.toString());
                deploymentTemplate = deploymentTemplate.replace("<VERSION>", versionId);

                kubeYamlData.setDeploymentEditableYaml(deploymentTemplate);

                //appsV1beta1Api.createNamespacedDeployment(nameSpace, AppsV1beta1Deployment, false, null, null);
            }
            kubeYamlData.setYamlFilePath("");

        } else {
            kubeYamlData.setYamlFilePath(yamlFilePath);
            kubeYamlData.setDeploymentEditableYaml("");
        }

        this.kubeYamlRepository.saveAndFlush(kubeYamlData);

    }

    /**
     * 判断Yaml是否存在
     */
    public Boolean isExistYamlData(Long appEnvId) {
        Long yamlCount = this.kubeYamlRepository.countByAppEnvId(appEnvId);
        return yamlCount.equals(new Long(1));
    }

    public KubeYamlDataBoV1 findYamlDataByEnvId(Long loginUser, Long appEnvId) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewYamlData(), loginUser)
                , BizCodes.NO_PERMISSION);
        KubeYamlData kubeYamlData = this.kubeYamlRepository.findByAppEnvId(appEnvId).orElse(null);
        if (kubeYamlData == null)
            return null;
        else
            return BeanUtils.convertType(kubeYamlData, KubeYamlDataBoV1.class);
    }


    public void updateDeploymentYaml(Long loginUser, Long appEnvId, String deploymentYaml) {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getEditDeploymentYaml(), loginUser)
                , BizCodes.NO_PERMISSION);
        KubeYamlData kubeYamlData = this.kubeYamlRepository.findByAppEnvId(appEnvId).orElse(null);
        kubeYamlData.setMtime(LocalDateTime.now());
        kubeYamlData.setMuser(loginUser);
        kubeYamlData.setDeploymentEditableYaml(deploymentYaml);
        this.kubeYamlRepository.saveAndFlush(kubeYamlData);
    }


    /**
     * 根据id获取client
     *
     * @param id 应用环境id
     * @return ApiClient
     */
    public ApiClient getClient(Long id) {
        KubeCredentialBoV1 kubeCredentialBoV1 = this.kubeCredentialService.queryByAppEnvId(id);
        String url = kubeCredentialBoV1.getTargetClusterUrl();
        String token = kubeCredentialBoV1.getTargetClusterToken();
        ApiClient client = Config.fromToken(url,
                token,
                false);
        return client;

    }


    /**
     * 根据id获取api
     *
     * @param id 应用环境id
     * @return NetworkingV1Api
     */
    public ExtensionsV1beta1Api getExtensionsV1beta1Api(Long id) {

        return new ExtensionsV1beta1Api(getClient(id));
    }


    /**
     * 根据id获取api
     *
     * @param id 应用环境id
     * @return CoreV1Api
     */
    public CoreV1Api getCoreV1Api(Long id) {

        return new CoreV1Api(getClient(id));
    }


    /**
     * 根据id获取api
     *
     * @param id 应用环境id
     * @return AppsV1beta1Api
     */
    public AppsV1beta1Api getAppsV1beta1Api(Long id) {
        getClient(id);
        return new AppsV1beta1Api(getClient(id));
    }

    /**
     * 创建服务
     *
     * @param id        应用环境id
     * @param namespace 命名空间
     * @param name      服务
     * @param port      容器
     */
    public void createServiceByNameSpace(Long loginUser, Long id, String namespace, String name, Integer port, Integer nodePort, String host) throws Exception {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getCreateService(), loginUser)
                , BizCodes.NO_PERMISSION);
        CoreV1Api coreV1Api = getCoreV1Api(id);

        AppsV1beta1Api AppsV1beta1Api = getAppsV1beta1Api(id);
        //AppsV1beta1Deployment deployment = new AppsV1beta1DeploymentBuilder()
        //        .withNewMetadata()
        //        .withName("test-deployment2")
        //        .addToLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
        //        .endMetadata()
        //        .withNewSpec()
        //        .withReplicas(2)
        //        .withNewSelector()
        //        .addToMatchLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(),name)
        //        .endSelector()
        //        .withNewTemplate()
        //        .withNewMetadata()
        //        .addToLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
        //        .endMetadata()
        //        .withNewSpec()
        //        .addNewContainer()
        //        .withName(name)
        //        .withImage( "registry.dop.clsaa.com/dop/dop-web:3")
        //        .endContainer()
        //        .addNewContainer()
        //        .withName("test-container")
        //        .withImage( "registry.dop.clsaa.com/dop/dop-web:4")
        //        .endContainer()
        //        .endSpec()
        //        .endTemplate()
        //        .endSpec()
        //        .build();
        //
        //AppsV1beta1Deployment deployment2 = new AppsV1beta1DeploymentBuilder()
        //        .withNewMetadata()
        //        .withName("test-deployment")
        //        .addToLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
        //        .endMetadata()
        //        .withNewSpec()
        //        .withReplicas(2)
        //        .withNewSelector()
        //        .addToMatchLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(),name)
        //        .endSelector()
        //        .withNewTemplate()
        //        .withNewMetadata()
        //        .addToLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
        //        .endMetadata()
        //        .withNewSpec()
        //        .addNewContainer()
        //        .withName(name)
        //        .withImage("registry.dop.clsaa.com/dop/dop-web:5")
        //        .endContainer()
        //        .endSpec()
        //        .endTemplate()
        //        .endSpec()
        //        .build();
        //
        //V1ReplicationController replicationController =
        //        new V1ReplicationControllerBuilder()
        //                .withNewMetadata()
        //                .withName(name)
        //                .endMetadata()
        //                .withNewSpec()
        //                .withReplicas(replicas.intValue())
        //                .addToSelector(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
        //                .withNewTemplate()
        //                .withNewMetadata()
        //                .withName(name)
        //                .addToLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
        //                .endMetadata()
        //                .withNewSpec()
        //                .addNewContainer()
        //                .withName(name)
        //                .withImage(image)
        //                .addNewPort()
        //                .withContainerPort(port.intValue())
        //                .endPort()
        //                .endContainer()
        //                .endSpec()
        //                .endTemplate()
        //                .endSpec()
        //                .build();

        if (host == null) {
            V1Service service =
                    new V1ServiceBuilder()
                            .withApiVersion(kubernetesDefaultConfig.getServiceApiVersion())
                            .withKind(kubernetesDefaultConfig.getServiceKind())
                            .withNewMetadata()
                            .withName(name)
                            .withNamespace(namespace)
                            .addToLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
                            .endMetadata()
                            .withNewSpec()
                            .withType(kubernetesDefaultConfig.getServiceTypeNodeport())
                            .addNewPort()
                            .withName(kubernetesDefaultConfig.getServiceNodeportHttpName())
                            .withPort(port)
                            .withTargetPort(new IntOrString(port))
                            .withProtocol(kubernetesDefaultConfig.getDeploymentContainerPortProtocol())
                            .withNodePort(nodePort)
                            .endPort()
                            .addToSelector(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
                            .endSpec()
                            .build();
            //api.createNamespacedReplicationController(namespace,replicationController,null,null,null);
            //     AppsV1beta1Api.createNamespacedDeployment(namespace,deployment,false,null,null);
            //AppsV1beta1Api.createNamespacedDeployment(namespace,deployment2,false,null,null);
            coreV1Api.createNamespacedService(namespace, service, false, null, null);
        } else {
            V1Service service =
                    new V1ServiceBuilder()
                            .withApiVersion(kubernetesDefaultConfig.getServiceApiVersion())
                            .withKind(kubernetesDefaultConfig.getServiceKind())
                            .withNewMetadata()
                            .withName(name)
                            .withNamespace(namespace)
                            .addToLabels(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
                            .endMetadata()
                            .withNewSpec()
                            .addNewPort()
                            .withPort(port)
                            .withTargetPort(new IntOrString(port))
                            .withProtocol(kubernetesDefaultConfig.getDeploymentContainerPortProtocol())
                            .endPort()
                            .addToSelector(kubernetesDefaultConfig.getDeploymentLabelPrefix(), name)
                            .endSpec()
                            .build();
            //api.createNamespacedReplicationController(namespace,replicationController,null,null,null);
            //     AppsV1beta1Api.createNamespacedDeployment(namespace,deployment,false,null,null);
            //AppsV1beta1Api.createNamespacedDeployment(namespace,deployment2,false,null,null);
            coreV1Api.createNamespacedService(namespace, service, false, null, null);

            V1beta1Ingress ingress =
                    new V1beta1IngressBuilder()
                            .withApiVersion(kubernetesDefaultConfig.getIngressApiVersion())
                            .withKind(kubernetesDefaultConfig.getIngressKind())
                            .withNewMetadata()
                            .withName(name)
                            .withNamespace(namespace)
                            .endMetadata()
                            .withNewSpec()
                            .addNewRule()
                            .withHost(host)
                            .withNewHttp()
                            .addNewPath()
                            .withNewBackend()
                            .withServiceName(name)
                            .withServicePort(new IntOrString(port))
                            .endBackend()
                            .endPath()
                            .endHttp()
                            .endRule()
                            .endSpec()
                            .build();

            ExtensionsV1beta1Api extensionsV1beta1Api = getExtensionsV1beta1Api(id);
            extensionsV1beta1Api.createNamespacedIngress(namespace, ingress, false, null, null);
            //V1NetworkPolicy v1NetworkPolicy=new V1NetworkPolicy(ingress);
            //networkingV1Api.createNamespacedNetworkPolicy(namespace,ingress);
        }

    }

    /**
     * 获取该应用对应的cluster的所有命名空间
     *
     * @param id 应用环境id
     * @return {@link List<String>}
     */
    public List<String> findNameSpaces(Long loginUser, Long id) throws Exception {
        //校验权限
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewNameSpace(), loginUser)
                , BizCodes.NO_PERMISSION);
        //根据用户所填集群信息获取api
        CoreV1Api api = getCoreV1Api(id);
        //查询所有命名空间
        return api.listNamespace(true, null, null, null, null, null, null, null, false)
                .getItems()
                .stream()
                .map(v1Namespace -> v1Namespace.getMetadata().getName())
                .collect(Collectors.toList());

    }

    /**
     * 根据命名空间获取服务列表
     *
     * @param id        应用环境id
     * @param namespace 命名空间
     * @return {@link List<String>}
     */
    public List<String> getServiceByNameSpace(Long loginUser, Long id, String namespace) throws Exception {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewService(), loginUser)
                , BizCodes.NO_PERMISSION);
        CoreV1Api api = getCoreV1Api(id);

        return api.listNamespacedService(namespace, false, null, null, null, null, Integer.MAX_VALUE, null, null, false)
                .getItems()
                .stream()
                .map(v1Service -> v1Service.getMetadata().getName())
                .collect(Collectors.toList());
    }

    /**
     * 根据命名空间及服务名称获取部署
     *
     * @param id        应用环境id
     * @param namespace 命名空间
     * @param service   服务
     * @return {@link List<String>}
     */
    public HashMap<String, Object> getDeploymentByNameSpaceAndService(Long loginUser, Long id, String namespace, String service) throws Exception {
        BizAssert.authorized(this.permissionService.checkPermission(permissionConfig.getViewDeployment(), loginUser)
                , BizCodes.NO_PERMISSION);
        AppsV1beta1Api api = getAppsV1beta1Api(id);


        AppsV1beta1DeploymentList deploymentList = api.listNamespacedDeployment(namespace, false, null, null, null, "app=" + service, Integer.MAX_VALUE, null, null, false);

        List<AppsV1beta1Deployment> AppsV1beta1DeploymentList = deploymentList.getItems();

        List<String> nameList = AppsV1beta1DeploymentList.stream().map(AppsV1beta1Deployment -> AppsV1beta1Deployment.getMetadata().getName()).collect(Collectors.toList());
        Map<String, List<String>> containerList = new HashMap<>();
        List<List<String>> lists = deploymentList.getItems().stream().map(
                AppsV1beta1Deployment -> AppsV1beta1Deployment.getSpec().getTemplate().getSpec().getContainers().stream().map(
                        v1Container -> v1Container.getName()).collect(Collectors.toList()
                )).collect(Collectors.toList());

        for (int i = 0; i < nameList.size(); i++) {
            containerList.put(nameList.get(i), AppsV1beta1DeploymentList.get(i).getSpec().getTemplate().getSpec().getContainers().stream().map(
                    v1Container -> v1Container.getName()).collect(Collectors.toList()
            ));
        }


        return new HashMap<String, Object>() {
            {
                put("deployment", nameList);
                put("containers", containerList);
            }

        };
    }

}
