## Mysql Configure

## download and install
```cmd
# 推荐直接使用所使用的linux版本包管理器直接安装
# 如, ubuntu:
> apt-get update && apt-get install mysql-server mysql-client -y

# 启动服务器
> systemctl enable mysql
> systemctl start mysql
```

## set
```cmd
# 登录
> mysql

# 创建数据库
> create database sns

# 切换数据库
> use database

# 创建用户表
> create table users(user varchar(20), password varchar(20));

# 给root用户配置密码,其中 root为用户名，admin@123 为实际的密码，根据需要设置
> set password for root@localhost = password('admin@123'); 
## 创建新的用户，其中 gogeof为用户名，admin@123 为实际的密码，根据需要设置
> create user 'gogeof'@'host' identified by 'admin@123';

# 授权
> grant privileges on databasename.tablename to 'username'@'host' 
```

