### Redis事物
简单介绍下Redis的几个事物命令:

redis事物四大指令: MULTI(开启事务)、EXEC(提交)、DISCARD(回滚)、WATCH。

这四个指令构成了redis事务处理的基础。
1. MULTI用来组装一个事务
2. EXEC用来执行一个事务
3. DISCARD用来取消一个事务
4. WATCH类似于乐观锁机制里的版本号。

被WATCH的key如果在事务执行过程中被并发修改，则事务失败。需要重试或取消。

SpringDataRedis提供了2种食物的支持方式：

#### 1.execute(SessionCallback session)方法:
```
<T> T execute(SessionCallback<T> session);
```

#### 2. @Transactional的支持
另一种实现事务的是@Transactional注解，这种方法是把事务交由给spring事务管理器进行自动管理。使用这种方法之前，跟jdbc事务一样，要先注入事务管理器，如果工程中已经由配置了
事务管理器，就可以复用这个事务管理器。，不用另外进行配置。另外，需要注意的是，跟第一种事务操作方法不一样的地方就是RedisTemplate的setEnableTransactionSupport(boolean enableTransactionSupport)方法要set为true，此处贴出官方的配置框架：

