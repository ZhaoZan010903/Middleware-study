#### Redis整合

```xml
<!--Spring Boot 整合Redis依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
```yaml
Spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      username: ~
      password: ~
      # 数据库分区 
      database: 1
# Redis一共有16个分区 DB 0 -- DB 15
```
