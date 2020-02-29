### mongoDB 部署

### 创建PV


```
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mongodb-pv
spec:
  accessModes:
    - ReadWriteOnce     #指定访问模式
  capacity:
    storage: 1Gi    #存储容量 2G
  persistentVolumeReclaimPolicy: Retain   #回收策略 Retain 管理员手工回收 Recycle  
  #清除 PV 的数据  Delete  删除Storage Provider上的对应存储资源
  storageClassName: nfs   #指定PV 的class为nfs
  nfs:
    path: /root/k8s/nfsdata/mongodb    #PV在 NFS服务器上对应的目录
    server: 172.31.164.123      #要连接的主机地址
```
创建后执行即可
```
kubectl apply -f mongodb-pv.yml
```

### 创建 PVC

```
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: mongodb-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 2Gi
  storageClassName: nfs
```
创建后执行即可
```
kubectl apply -f mongodb-pvc.yml
```

### 部署 MongoDB

这里 MongoDB的service采用NodePort方式部署，外部可以访问

```
apiVersion: v1
kind: Service
metadata:
  name: mongo
  labels:
    app: mongo
spec:
  ports:
  - name: mongo
    port: 27017
    targetPort: 27017
  selector:
    app: mongo
---
apiVersion: v1
kind: Service
metadata: 
  name: mongo-service
  labels: 
    app: mongo
spec: 
  ports: 
  - name: mongo-http
    port: 27017
  selector: 
    app: mongo
  type: NodePort
---
apiVersion: apps/v1
kind: StatefulSet
metadata: 
  name: mongo
spec: 
  selector: 
    matchLabels: 
      app: mongo
  serviceName: "mongo"
  replicas: 2
  podManagementPolicy: Parallel
  template: 
    metadata: 
      labels: 
        app: mongo
    spec: 
      terminationGracePeriodSeconds: 10
      affinity: 
         podAntiAffinity: 
           requiredDuringSchedulingIgnoredDuringExecution: 
           - labelSelector: 
               matchExpressions: 
               - key: "app"
                 operator: In
                 values: 
                 - mongo
             topologyKey: "kubernetes.io/hostname"
      containers: 
      - name: mongo
        image: mongo
        command:  
        - mongod 
        - "--bind_ip_all"
        - "--replSet"
        - rs0
        ports: 
        - containerPort: 27017
        volumeMounts: 
        - name: mongodb-persistent-storage
          mountPath: /var/lib/mongodb
      volumes:
      - name: mongodb-persistent-storage
        persistentVolumeClaim:
          claimName: mongodb-pvc
```

创建后执行即可
```
kubectl apply -f mongo.yml
```
