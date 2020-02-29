# Helm的安装
HELM可以看做Centos下的yum
- HELM 客户端工具，用户kubernetes应用CHart的创建、打包、发布以及创建和管理本地和远程的Chart仓库

- Tiller
接受请求，根据chart生成部署文件，并提交给k8s创建应用

- Chart 软件包包含了一系列相关的yaml文件

- Repository 软件仓库 包含一系列chart供用户下载

- Release 在K8s集群中部署的Chart被称为release

<img src= https://www.hi-linux.com/img/linux/helm02.png alt="Helm工作原理"/>

## 安装步骤
1. 下载helm安装的sh文件
https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get
2. 赋权并安装
  ```
   chmod 700 get_helm.sh  
   ./get_helm.sh
   ```
3. 安装
  ```
  # 使用阿里云镜像安装并把默认仓库设置为阿里云上的镜像仓库
  $ helm init --upgrade --tiller-image registry.cn-hangzhou.aliyuncs.com/google_containers/tiller:v2.15.2 --stable-repo-url https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts

  ```
4. 给 Tiller 授权
  ```
  #创建 Kubernetes 的服务帐号和绑定角色
  kubectl create serviceaccount --namespace kube-system tiller

  kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
 ```

5. 为 Tiller 设置帐号
```
  # 使用 kubectl patch 更新 API 对象
  kubectl patch deploy --namespace kube-system tiller-deploy -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}'
```
6. 查看是否授权成功
```
  kubectl get deploy --namespace kube-system   tiller-deploy  --output yaml|grep  serviceAccount
  
  #验证Tiller是否安装成功

  kubectl -n kube-system get pods|grep tiller
  helm version
  ```