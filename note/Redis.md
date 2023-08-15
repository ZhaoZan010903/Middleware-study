### Redis命令

##### 一、Reids连接客户端
1. Redis客户端的基本语法为：
```shell
redis-cli
```

2. 在远程服务上执行命令
```shell
redis-cli -h host -p port -a password
```

3. _补充_:

可以避免中文乱码问题
```shell
redis-cli --raw
```



##### 二、Redis键(key)
Redis 键命令用于管理redis的键
