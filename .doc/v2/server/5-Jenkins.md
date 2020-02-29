# 使用HELM安装Jenkins

## 安装步骤
1. 添加阿里云的仓库
```
helm repo add apphub https://apphub.aliyuncs.com/
```
2. 拉取chart文件夹
```
helm fetch apphub/jenkins
```
3. 修改values.yaml
```
persistence:
  enabled: true
  ## If defined, volume.beta.kubernetes.io/storage-class: <storageClass>
  ## Default: volume.alpha.kubernetes.io/storage-class: default
  ##
  #把storageClass改成nfs
  storageClass: nfs
  accessMode: ReadWriteOnce
  size: 10Gi
```
4. 创建jenkins-pv.yaml
```
apiVersion: v1
kind: PersistentVolume
metadata:
   name: jenkins
   namespace: jenkins
spec:
   capacity:
     storage: 10Gi
   accessModes:
     - ReadWriteOnce
   persistentVolumeReclaimPolicy: Retain
   storageClassName: nfs
   volumeMode: Filesystem
   nfs:
     # server为内网ip地址 path地址要保证打开可以访问（见mysql）
     path: /root/k8s/nfsdata/jenkins
     server: 172.31.164.123                                    
```
5. 使用helm进行安装
```
[root@dop-node1 jenkins]#  helm install -n jenkins --namespace jenkins --values values.yaml apphub/jenkins
NAME:   jenkins
LAST DEPLOYED: Sun Nov 17 13:43:39 2019
NAMESPACE: jenkins
STATUS: DEPLOYED

RESOURCES:
==> v1/Deployment
NAME     READY  UP-TO-DATE  AVAILABLE  AGE
jenkins  0/1    1           0          1s

==> v1/PersistentVolumeClaim
NAME     STATUS  VOLUME   CAPACITY  ACCESS MODES  STORAGECLASS  AGE
jenkins  Bound   jenkins  10Gi      RWO           nfs           1s  Filesystem

==> v1/Pod(related)
NAME                      READY  STATUS             RESTARTS  AGE
jenkins-594cbc6448-lvlgj  0/1    ContainerCreating  0         1s

==> v1/Secret
NAME     TYPE    DATA  AGE
jenkins  Opaque  1     1s

==> v1/Service
NAME     TYPE          CLUSTER-IP   EXTERNAL-IP  PORT(S)                     AGE
jenkins  LoadBalancer  10.1.79.225  <pending>    80:32757/TCP,443:30761/TCP  1s


NOTES:

** Please be patient while the chart is being deployed **

1. Get the Jenkins URL by running:

** Please ensure an external IP is associated to the jenkins service before proceeding **
** Watch the status using: kubectl get svc --namespace jenkins -w jenkins **

  export SERVICE_IP=$(kubectl get svc --namespace jenkins jenkins --template "{{ range (index .status.loadBalancer.ingress 0) }}{{.}}{{ end }}")
  echo "Jenkins URL: http://$SERVICE_IP/"

2. Login with the following credentials

  echo Username: user
  echo Password: $(kubectl get secret --namespace jenkins jenkins -o jsonpath="{.data.jenkins-password}" | base64 --decode)



[root@dop-node1 jenkins]# echo Username: user
Username: user
[root@dop-node1 jenkins]# echo Password: $(kubectl get secret --namespace jenkins jenkins -o jsonpath="{.data.jenkins-password}" | base64 --decode)
Password: kUTvK1tMFL

```
用户名：user
密码：111111


6. 创建jenkins-pv
```
 kubectl create -f jenkins-pv.yaml

```

7. 查看deployment pod svc
```
[root@dop-node1 jenkins]# kubectl get pods -n jenkins
NAME                       READY   STATUS    RESTARTS   AGE
jenkins-594cbc6448-lvlgj   1/1     Running   0          48s
[root@dop-node1 jenkins]# kubectl get svc -n jenkins
NAME      TYPE           CLUSTER-IP    EXTERNAL-IP   PORT(S)                      AGE
jenkins   LoadBalancer   10.1.79.225   <pending>     80:32757/TCP,443:30761/TCP   76s
[root@dop-node1 jenkins]# kubectl get pod,deployment,svc -n jenkins
NAME                           READY   STATUS    RESTARTS   AGE
pod/jenkins-594cbc6448-lvlgj   1/1     Running   0          2m32s

NAME                      READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/jenkins   1/1     1            1           2m32s

NAME              TYPE           CLUSTER-IP    EXTERNAL-IP   PORT(S)                      AGE
service/jenkins   LoadBalancer   10.1.79.225   <pending>     80:32757/TCP,443:30761/TCP   2m32s

```

8. 使用node3IP:nodePort访问
http://115.28.186.77:32757/


## 本机安装jenkins
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key

yum install jenkins