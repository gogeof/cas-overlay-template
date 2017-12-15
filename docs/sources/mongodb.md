## Mongodb Configure

## download
```cmd
# 推荐到官网下载最新版mongodb
# 已测试版本： 3.6.0版本
curl -O https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-ubuntu1604-3.6.0.tgz
```

## install
```cmd
# 解压
>tar -xzf mongodb-linux-xxx.tgz
# 进入到mongodb/bin目录下，执行如下命令启动：
>mongod
```

## set
```cmd
#登录
>mongo

#切换数据库
>use admin

#新增管理员，需与management配置文件中配置的用户名密码一致，否则将导致management无法启动
>db.createUser({user: "admin",pwd: "123456",roles:[{role:"userAdminAnyDatabase", db: "admin" } ]})

#切换数据库
>use cas-mongo-database

# 新增用户
>db.createUser({user: "cas-config",pwd: "123456",roles: [ { role: "readWrite", db: "cas-mongo-database" }]})

#重启并开启认证
>mongod --auth
```