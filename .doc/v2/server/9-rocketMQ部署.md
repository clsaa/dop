# rocketMQ部署及邮件服务启动

## 下载

http://rocketmq.apache.org/release_notes/

传到服务器上

安装unzip

yum install unzip

unzip 	rocketmq-all-4.6.0-bin-release.zip

进入文件夹

修改**`conf/broker.conf`**

添加brokerIP1 = 121.42.13.103(公网端口)



修改**`runserver.sh`**和**`runbroker.sh`**

```java
JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn125m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
```
## 运行
1. nameserver
```java
nohup sh bin/mqnamesrv &
```
>查看运行日志：tail -f ~/logs/rocketmqlogs/namesrv.log

### 2.4. 运行Broker

```
nohup sh bin/mqbroker -n 121.42.13.103:9876 -c conf/broker.conf &
```

> 通过-c参数指定配置文件 查看运行日志：tail -f ~/logs/rocketmqlogs/broker.log

### 2.5. 停止服务的方式

```java
sh bin/mqshutdown broker

sh bin/mqshutdown namesrv
```
## 配置rocketMQ
```java
//application.yml
namesrvAddr: rocketmq.dop.clsaa.com:9876 
```
## 配置messageServer
在qq邮箱中开启smtp授权
```java
//application.yml
host: smtp.qq.com
    username: 1740122439@qq.com
    password: rfbyspwizxvcdejj
```

## 待解决问题
redis服务无法启动，无法将密码写入数据库
