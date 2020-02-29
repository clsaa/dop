## Docker

> 在 master 节点和 node 节点都要执行

#### 删除旧版本的docker

```
sudo yum remove -y docker \
docker-client \
docker-client-latest \
docker-common \
docker-latest \
docker-latest-logrotate \
docker-logrotate \
docker-selinux \
docker-engine-selinux \
docker-engine
```

#### 安装所需的包

> yum-utils提供了yum-config-manager 效用，devicemapper存储驱动程序由需要 device-mapper-persistent-data和lvm2

```
 yum install -y yum-utils device-mapper-persistent-data  lvm2
```

#### 设置稳定的存储库

```
yum-config-manager  --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

#### （可选）启用夜间或测试存储库

> 这些存储库包含在docker.repo上面的文件中，但默认情况下处于禁用状态。可以将它们与稳定存储库一起启用。

```
# 以下命令启用nightly存储库。
yum-config-manager --enable docker-ce-nightly
# 要启用test通道，请运行以下命令
yum-config-manager --enable docker-ce-test

### 可以通过运行带有 --disable 的 yum-config-manager 命令来禁用nightly或test存储库 。要重新启用它，请使用 --enable 。以下命令禁用nightly存储库
yum-config-manager --disable docker-ce-nightly
```

#### 安装Docker ENGINE - COMMUNITY

```
yum install docker-ce docker-ce-cli containerd.io
```

#### 启动Docker

```
systemctl start docker
#查看是否启动成功
service docker status
#通过运行hello-world 映像验证是否正确安装
docker run hello-world
```

#### 设置开机启动Docker

```
systemctl enable docker && systemctl restart docker && service docker status
```

#### 配置docker启动参数

```
vim /etc/docker/daemon.json
```

写入

```
{
  "registry-mirrors": ["https://registry.docker-cn.com"],
  "exec-opts": ["native.cgroupdriver=systemd"]
}
```

重启docker

```
systemctl  daemon-reload
systemctl  restart docker
```

