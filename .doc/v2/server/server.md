## 实验室服务器配置V2

### 1.1. 服务器基本信息

> 采用密钥登陆的方式

##### 1.1.1. dop-node1

- ip-121.42.13.243
- master节点
- 地域：青岛
- 内网IP（同一region内，内网ip互通）：172.31.164.123
- hosts

```
::1     localhost       localhost.localdomain   localhost6      localhost6.localdomain6
127.0.0.1       localhost       localhost.localdomain   localhost4      localhost4.localdomain4

172.31.164.123  iZm5e20kp0myoxxo8qtezdZ iZm5e20kp0myoxxo8qtezdZ

```

- 安全组
  - 端口：1~65535
  - 上行：%
  - 下行：%

##### 1.1.2 dop-node2

- ip-121.42.13.103
- node节点
- 地域：青岛
- 内网IP（同一region内，内网ip互通）：172.31.207.94
- hosts

```
::1     localhost       localhost.localdomain   localhost6      localhost6.localdomain6
127.0.0.1       localhost       localhost.localdomain   localhost4      localhost4.localdomain4

172.31.207.94   iZm5e06mxhbe3ksnz3tamhZ iZm5e06mxhbe3ksnz3tamhZ


```

- 安全组
  - 端口：1~65535
  - 上行：%
  - 下行：%

##### 1.1.3 dop-node3

- ip-115.28.186.77
- node节点
- 地域：青岛
- 内网IP（同一region内，内网ip互通）：172.31.36.158
- hosts

```
::1     localhost       localhost.localdomain   localhost6      localhost6.localdomain6
127.0.0.1       localhost       localhost.localdomain   localhost4      localhost4.localdomain4

172.31.36.158   iZm5ej4i2jdf3jrmbz2huwZ iZm5ej4i2jdf3jrmbz2huwZ

```

- 安全组
  - 端口：1~65535
  - 上行：%
  - 下行：%