## 1. 实验室服务器配置
### 1.1. 服务器基本信息
##### 1.1.1. node001
+ ip-121.43.191.226
+ hostname:softeng-cn-hz-2c8g-node001
+ 地域：杭州
+ 可用区：可用区H
+ 内网IP（同一region内，内网ip互通）：172.16.24.194
+ hosts
```
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
127.0.0.1 localhost
172.16.24.194 softeng-cn-hz-2c8g-node001
172.16.24.195 softeng-cn-hz-2c8g-node002
172.16.24.196 softeng-cn-hz-2c8g-node003

172.16.24.194 jenkins.dop.clsaa.com
172.16.24.195 registry.dop.clsaa.com
172.16.24.196 gitlab.dop.clsaa.com
```

+ 安全组   
    + 端口：1~30010
    + 上行：%
    + 下行：%
##### 1.1.2 node002
+ ip-47.99.138.113
+ hostname:softeng-cn-hz-2c8g-node002
+ 地域：杭州
+ 可用区：可用区H
+ 内网IP（同一region内，内网ip互通）：172.16.24.195
+ hosts
```
127.0.0.1 localhost
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
172.16.24.194 softeng-cn-hz-2c8g-node001
172.16.24.195 softeng-cn-hz-2c8g-node002
172.16.24.196 softeng-cn-hz-2c8g-node003


172.16.24.194 jenkins.dop.clsaa.com
172.16.24.195 registry.dop.clsaa.com
172.16.24.196 gitlab.dop.clsaa.com
```

+ 安全组
    + 端口：1~30010
    + 上行：%
    + 下行：%
##### 1.1.3 node003
+ ip-47.110.147.92
+ hostname:softeng-cn-hz-2c8g-node003
+ 地域：杭州
+ 可用区：可用区H
+ 内网IP（同一region内，内网ip互通）：172.16.24.196
+ hosts
```
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
127.0.0.1 localhost
172.16.24.194 softeng-cn-hz-2c8g-node001
172.16.24.195 softeng-cn-hz-2c8g-node002
172.16.24.196 softeng-cn-hz-2c8g-node003

172.16.24.194 jenkins.dop.clsaa.com
172.16.24.195 registry.dop.clsaa.com
172.16.24.196 gitlab.dop.clsaa.com
```
+ 安全组
    + 端口：1~30010
    + 上行：%
    + 下行：%
+ 部署应用
    + nginx
        + port：80
        + config：/etc/nginx/nginx.conf
    + gitlab
        + port(in docker):0.0.0.0:22->22/tcp, 0.0.0.0:8080->80/tcp, 0.0.0.0:8443->443/tcp
    + ssh
        + port：2222

## 2. kubernetes
### 2.1. 已部署应用
##### 2.1.1 ingress
##### 2.1.2 dashboard
+ 地址：https://dashboard.k8s.dop.clsaa.com
+ token
```
eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC10b2tlbi1sY25kOCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImIyZDBlYTQzLTA5MzAtMTFlOS1hYmM3LTAwMTYzZTBlYzFjZiIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWRhc2hib2FyZCJ9.KlrkaUDeoyWngUwbmGS2C7gpSixEYJYRgv52w9v_YVLe_uDO_SdHAaQanxG8W23RbKxYPRt_0S7haFy-gU5ngbuYPxHVvPMoB8gVrPX8dGOvYpxvs26eOEjibgnfJTmegWBgylSP9ULKqLTgJ3feFiUyMtd_metvaCSJInPDonDFlvNTzLIn8sOxE3Qxq3fAApNgkxNeuHT8vygznoLysv0I3Tzobhn5R78q5D1QL01AxRlAIKm57i6h5X7utoXrnt8JbuLlMk2ZERa8ANTlhTDhFOj4ODiAqWgN2gtDUmX9ACGHr7kbU8HW_COj4QMS6gLNdnI4bBxTCWVSL-er9Q
```
##### 2.1.3. master
+ 121.43.191.226
##### 2.1.4. slave
+ 47.99.138.113
+ 47.110.147.92
## 3. nginx
+ 部署ip：47.110.147.92
+ port：80
+ config file path：/etc/nginx
## 4. gitlab
+ domain：http://gitlab.dop.clsaa.com
+ 部署ip：47.110.147.92
+ port(in docker):0.0.0.0:22->22/tcp, 0.0.0.0:8080->80/tcp, 0.0.0.0:8443->443/tcp
```
#!/bin/bash
HOST_NAME=gitlab.dop.clsaa.com
GITLAB_DIR=/mnt/gitlab
docker stop gitlab
docker rm gitlab
docker run -d \
    --hostname ${HOST_NAME} \
    -p 8443:443 -p 8080:80 -p 22:22 \
    --name gitlab \
    -v ${GITLAB_DIR}/config:/etc/gitlab \
    -v ${GITLAB_DIR}/logs:/var/log/gitlab \
    -v ${GITLAB_DIR}/data:/var/opt/gitlab \
    gitlab/gitlab-ce:latest
```
## 5. jenkins
+ domain：http://jenkins.dop.clsaa.com/
+ 部署ip：121.43.191.226
+ port：8976
+ 命令：
```
nohup java -jar jenkins.war --httpPort=8976 /var/log/jenkins/running-log &
```
## 6. harbor
+ domain：https://registry.dop.clsaa.com/
+ 部署ip：47.99.138.113
+ ./package/harbor/install.sh
+ Dop123456789
+ port(run in docker)：0.0.0.0:80->80/tcp, 0.0.0.0:443->443/tcp, 0.0.0.0:4443->4443/tcp
```
version: '2'
services:
  log:
    image: goharbor/harbor-log:v1.7.0
    container_name: harbor-log 
    restart: always
    dns_search: .
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - DAC_OVERRIDE
      - SETGID
      - SETUID
    volumes:
      - /mnt/harbor/data/log:/var/log/docker/:z
      - ./common/config/log/:/etc/logrotate.d/:z
    ports:
      - 127.0.0.1:1514:10514
    networks:
      - harbor
  registry:
    image: goharbor/registry-photon:v2.6.2-v1.7.0
    container_name: registry
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - SETGID
      - SETUID
    volumes:
      - /mnt/harbor/data/registry:/storage:z
      - ./common/config/registry/:/etc/registry/:z
      - ./common/config/custom-ca-bundle.crt:/harbor_cust_cert/custom-ca-bundle.crt:z
    networks:
      - harbor
    dns_search: .
    depends_on:
      - log
    logging:
      driver: "syslog"
      options:  
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "registry"
  registryctl:
    image: goharbor/harbor-registryctl:v1.7.0
    container_name: registryctl
    env_file:
      - ./common/config/registryctl/env
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - SETGID
      - SETUID
    volumes:
      - /mnt/harbor/data/registry:/storage:z
      - ./common/config/registry/:/etc/registry/:z
      - ./common/config/registryctl/config.yml:/etc/registryctl/config.yml:z
    networks:
      - harbor
    dns_search: .
    depends_on:
      - log
    logging:
      driver: "syslog"
      options:  
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "registryctl"
  postgresql:
    image: goharbor/harbor-db:v1.7.0
    container_name: harbor-db
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - DAC_OVERRIDE
      - SETGID
      - SETUID
    volumes:
      - /mnt/harbor/data/database:/var/lib/postgresql/data:z
    networks:
      - harbor
    dns_search: .
    env_file:
      - ./common/config/db/env
    depends_on:
      - log
    logging:
      driver: "syslog"
      options:  
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "postgresql"
  adminserver:
    image: goharbor/harbor-adminserver:v1.7.0
    container_name: harbor-adminserver
    env_file:
      - ./common/config/adminserver/env
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - SETGID
      - SETUID
    volumes:
      - /mnt/harbor/data/config/:/etc/adminserver/config/:z
      - /mnt/harbor/data/secretkey:/etc/adminserver/key:z
      - /mnt/harbor/data/:/data/:z
    networks:
      - harbor
    dns_search: .
    depends_on:
      - log
    logging:
      driver: "syslog"
      options:  
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "adminserver"
  core:
    image: goharbor/harbor-core:v1.7.0
    container_name: harbor-core
    env_file:
      - ./common/config/core/env
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - SETGID
      - SETUID
    volumes:
      - ./common/config/core/app.conf:/etc/core/app.conf:z
      - ./common/config/core/private_key.pem:/etc/core/private_key.pem:z
      - ./common/config/core/certificates/:/etc/core/certificates/:z
      - /mnt/harbor/data/secretkey:/etc/core/key:z
      - /mnt/harbor/data/ca_download/:/etc/core/ca/:z
      - /mnt/harbor/data/psc/:/etc/core/token/:z
      - /mnt/harbor/data/:/data/:z
    networks:
      - harbor
    dns_search: .
    depends_on:
      - log
      - adminserver
      - registry
    logging:
      driver: "syslog"
      options:  
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "core"
  portal:
    image: goharbor/harbor-portal:v1.7.0
    container_name: harbor-portal
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - SETGID
      - SETUID
      - NET_BIND_SERVICE
    networks:
      - harbor
    dns_search: .
    depends_on:
      - log
      - core
    logging:
      driver: "syslog"
      options:
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "portal"

  jobservice:
    image: goharbor/harbor-jobservice:v1.7.0
    container_name: harbor-jobservice
    env_file:
      - ./common/config/jobservice/env
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - SETGID
      - SETUID
    volumes:
      - /data/job_logs:/var/log/jobs:z
      - ./common/config/jobservice/config.yml:/etc/jobservice/config.yml:z
    networks:
      - harbor
    dns_search: .
    depends_on:
      - redis
      - core
      - adminserver
    logging:
      driver: "syslog"
      options:
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "jobservice"
  redis:
    image: goharbor/redis-photon:v1.7.0
    container_name: redis
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - SETGID
      - SETUID
    volumes:
      - /mnt/harbor/data/redis:/var/lib/redis
    networks:
      - harbor
    dns_search: .
    depends_on:
      - log
    logging:
      driver: "syslog"
      options:
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "redis"
  proxy:
    image: goharbor/nginx-photon:v1.7.0
    container_name: nginx
    restart: always
    cap_drop:
      - ALL
    cap_add:
      - CHOWN
      - SETGID
      - SETUID
      - NET_BIND_SERVICE
    volumes:
      - ./common/config/nginx:/etc/nginx:z
    networks:
      - harbor
    dns_search: .
    ports:
      - 80:80
      - 443:443
      - 4443:4443
    depends_on:
      - postgresql
      - registry
      - core
      - portal
      - log
    logging:
      driver: "syslog"
      options:  
        syslog-address: "tcp://127.0.0.1:1514"
        tag: "proxy"
networks:
  harbor:
    external: false
```

## 7. mongodb
+ ip：47.99.138.113
+ port：0.0.0.0:27017->27017/tcp
+ mount：~/mongo/db:/data/db
+ 121.42.13.103:31041
+ 命令：
```
docker run -p 27017:27017 -v ~/mongo/db:/data/db -v /etc/localtime:/etc/localtime -v /etc/timezone:/etc/timezone  --name mongodb -d mongo:latest
```


## 8. redis
+ ip：121.43.191.226
+ port：0.0.0.0:6379->6379/tcp
+ 命令：
```
docker pull redis
```
```
docker run -d -p 6379:6379 --name redis-test redis /bin/bash -c "/usr/local/bin/redis-server"
```

