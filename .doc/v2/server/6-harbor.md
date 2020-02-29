# Harbor
wget https://storage.googleapis.com/harbor-releases/release-1.8.0/harbor-offline-installer-v1.8.0.tgz

# 解压
tar xvf harbor-offline-installer-v1.8.0.tgz
vi value.yml
修改域名为 121.42.13.243
修改端口为 30000
./install

账号admin
密码Dop12345