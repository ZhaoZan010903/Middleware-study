### Redis Repository
使用Redis存储库可让您在Redis哈希中无缝转换和存储域对象、应用自定义映射策略并使用二级索引。

好处：根据各种属性作为查询条件、更加面向对象的方式去redis

限制：只能针对对象POJO 转换 哈希操作

**_注意：_** 只能使用在Redis 2，8.0以上的版本，且禁用事务

1. 在RedisConfig上添加注解
```java
@EnableRedisRepositories("cn.note.redis.repository")
```
2. 创建UserRepository并继承
```java
/**
 * CrudRepository 提供基本CRUD
 * PagingAndSortingRepository 基本CRUD提供分页排序
 *
 * 实现机制: JDK动态代理 , 调用对应jedis命令
 */
public interface UserRepository extends CrudRepository<User,Integer>,PagingAndSortingRepository<User,Integer>{

}
```

3. 在POJO类中添加注解  
```java
@RedisHash("redis") // 用来指定key
@Data
public class User implements Serializable {
    @Id 
    private Integer id;
    private String username;
}
```


4. 使用
```java
@SpringBootTest
public class redisRepository {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testAdd(){
        User user = new User();
        user.setId(1);
        user.setUsername("徐");
        userRepository.save(user);
    }

    @Test
    public void testSelect(){
        System.out.println(userRepository.findById(1));
    }

    @Test
    public void testDel(){
        userRepository.deleteById(1);
    }
}
```


