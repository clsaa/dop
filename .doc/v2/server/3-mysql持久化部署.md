## Mysql部署

### 关于持久化部署mysql数据库
  mysql数据库如果简单地部署在k8s集群上，当pods重启时，数据可能会造成丢失，经过查找资料，发现通过PV和PVC可以进行一个持久化的部署。
  
### PV、PVC
- PersistentVolume（持久卷） 和 PersistentVolumeClaim（持久卷申请）
- PersistentVolume (PV) 是外部存储系统中的一块存储空间，由管理员创建和维护。与 Volume 一样，PV 具有持久性，生命周期独立于 Pod。

- PersistentVolumeClaim (PVC) 是对 PV 的申请 (Claim)。PVC 通常由普通用户创建和维护。需要为 Pod 分配存储资源时，用户可以创建一个 PVC，指明存储资源的容量大小和访问模式（比如只读）等信息，Kubernetes 会查找并提供满足条件的 PV

### 1.什么是持久化？
本人找了好多文章都没有找到满意的答案，最后是从孙卫琴写的《精通Hibernate：Java对象持久化技术详解》中，看到如下的解释，感觉还是比较完整的。摘抄如下：

狭义的理解: “持久化”仅仅指把域对象永久保存到数据库中；广义的理解,“持久化”包括和数据库相关的各种操作。

●     保存：把域对象永久保存到数据库。

●     更新：更新数据库中域对象的状态。

●     删除：从数据库中删除一个域对象。

●     加载：根据特定的OID，把一个域对象从数据库加载到内存。

●     查询：根据特定的查询条件，把符合查询条件的一个或多个域对象从数据库加载内在存中。

### 2．为什么要持久化？
持久化技术封装了数据访问细节，为大部分业务逻辑提供面向对象的API。

● 通过持久化技术可以减少访问数据库数据次数，增加应用程序执行速度；

● 代码重用性高，能够完成大部分数据库操作；

● 松散耦合，使持久化不依赖于底层数据库和上层业务逻辑实现，更换数据库时只需修改配置文件而不用修改代码。
原文链接：https://blog.csdn.net/sunyadongwanghbjm/article/details/1765073

**考虑采用NFS结合PV PVC实现持久化存储**

### 配置nfs
k8s-master  nfs-server

k8s-node1  k8s-node2 nfs-client

所有节点安装nfs
```
yum install -y nfs-common nfs-utils 
```
在master节点创建共享目录
```
[root@k8s-master k8s]# mkdir nfsdata
```
授权共享目录
```
[root@k8s-master k8s]# chmod 666 nfsdata
```
编辑exports文件
```
[root@k8s-master k8s]# vi /etc/exports
```
输入如下内容
```
/root/k8s/nfsdata *(rw,no_root_squash,no_all_squash,sync)
```

启动rpc和nfs（注意顺序）
```
[root@k8s-master k8s]# systemctl start rpcbind
[root@k8s-master k8s]# systemctl start nfs
```
查看 某个节点上的 NFS服务器
```
showmount -e
```
### 创建PV


```
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mysql-pv
spec:
  accessModes:
    - ReadWriteOnce     #指定访问模式
  capacity:
    storage: 1Gi    #存储容量 1G
  persistentVolumeReclaimPolicy: Retain   #回收策略 Retain 管理员手工回收 Recycle  
  #清除 PV 的数据  Delete  删除Storage Provider上的对应存储资源
  storageClassName: nfs   #指定PV 的class为nfs
  nfs:
    path: /root/k8s/nfsdata/mysql    #PV在 NFS服务器上对应的目录
    server: 172.31.164.123      #要连接的主机地址
```
创建后执行即可
```
kubectl apply -f mysql-pv.yml
```

### 创建 PVC

```
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: mysql-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
  storageClassName: nfs
```
创建后执行即可
```
kubectl apply -f mysql-pvc.yml
```

### 部署 Mysql

这里 mysql的service采用NodePort方式部署，外部可以访问

```
apiVersion: v1
kind: Service
metadata:
  name: mysql
spec:
  type: NodePort
  ports:
  - protocol: TCP
    nodePort: 30306
    port: 3306
    targetPort: 3306
  selector:
    app: mysql
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
spec:
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - image: mysql:5.6
        name: mysql
        env:
        - name: MYSQL_ROOT_PASSWORD
          value: password
        ports:
        - containerPort: 3306
          name: mysql
        volumeMounts:
        - name: mysql-persistent-storage
          mountPath: /var/lib/mysql       #挂载目录
      volumes:
      - name: mysql-persistent-storage
        persistentVolumeClaim:
          claimName: mysql-pvc
```

创建后执行即可
```
kubectl apply -f mysql.yml
```

在主节点进入mysql数据库命令如下  10.1.70.69是mysql的服务在集群中的内部IP地址
```
kubectl run -it --rm --image=mysql:5.6 --restart=Never mysql-client -- mysql -h 10.1.70.69 -ppassword
```
此时的mysql 数据库部署在 node2节点上，外部连接时，地址为 {node2节点IP}:30306 数据库密码为 password
### 删除 mysql pv pvc
要删除 mysql的pod，首先要删除其deployment，然后再删除pod
要删除pv，首先要删除其pod，然后删除pvc，最后删除pv

### 进入mysql
在集群上查看容器id
ps -a
容器id 为3a4746c113b4
执行  docker exec -it 3a4746c113b4 /bin/bash
可以使用mysql命令
 mysql -h127.0.0.1 -uroot -ppassword
进入mysql
### mysql 信息
ip:port: 115.28.186.77:30306

用户名
dop
密码
Dop_mysql_12345

## 创建用户 和 数据库 并授权
create user "dop"@"%" identified by "Dop_mysql_12345";

create DATABASE db_dop_code;
create DATABASE db_dop_image;
create DATABASE db_dop_login;
create DATABASE db_dop_message;
create DATABASE db_dop_permission;
create DATABASE db_dop_pipeline;
create DATABASE db_dop_test;
create DATABASE db_dop_user;

grant all privileges on `db_dop_code`.* to 'dop'@'%';
grant all privileges on `db_dop_image`.* to 'dop'@'%';
grant all privileges on `db_dop_login`.* to 'dop'@'%';
grant all privileges on `db_dop_message`.* to 'dop'@'%';
grant all privileges on `db_dop_permission`.* to 'dop'@'%';
grant all privileges on `db_dop_pipeline`.* to 'dop'@'%';
grant all privileges on `db_dop_test`.* to 'dop'@'%';
grant all privileges on `db_dop_user`.* to 'dop'@'%';