### Redis中的数据类型
Redis支持5种数据类型: string(字符串),hash(哈希),list(列表),set(集合)以及Zset(sorted set:有序集合)



#### 1.String

string是Redis最基本的类型,一个key对应一个value.value 其实不仅是String,,也可以是数字,**_String类型的值最大能存储512MB_**
```redis
SET key value -- 存入字符串键值对
MSET key value -- 评论存储字符串键值对 (不支持)
SETNX key value -- 存入一个不存在的字符串键值对 
GET key -- 获取一个字符串的键值对 
MGET key [key....] -- 批量获取字符串键值 (不支持)
DEL key [key.....] -- 删除一个键
EXPIRE key secondes -- 设置一个键的过期时间

原子加减(原子性 将整个操作视为一个整体)
INCR key -- 将key中存储的数字值+1
DECR key -- 将key中存储的数字值-1
```


```java
@SpringBootTest
public class RedisString {
    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void StringTest(){
        // String类型
        BoundValueOperations username = redisTemplate.boundValueOps("username");
        //10秒过期
        username.set("徐庶",10, TimeUnit.SECONDS);
    }

    @Test
    public void StringTest01(){
        // String类型
        BoundValueOperations username = redisTemplate.boundValueOps("username");
        // 设置不存在的字符串 true=存入成功,false=存入失败
        System.out.println(username.setIfAbsent("徐庶"));
    }

    @Test
    public void StringTest02(){
        // 删除
        redisTemplate.delete("username");
    }
    
    @Test
    public void StringTest03(){
        ValueOperations count = redisTemplate.opsForValue();
        // 累加 不传参就是 +1
        // 并发情况下,也可以保证线程安全
        String key = "count";
        count.increment("count",11L);  //累加
        count.decrement("count",10L);  //累减

    }
    
    /**
     * 秒杀 1小时
     * 预热 100库存
     */
    @Test
    public void StringTest05(){
        BoundValueOperations stock = redisTemplate.boundValueOps("stock");
        // 预热100个库存 1小时过期
        stock.set(100,1,TimeUnit.HOURS);
    }
    
    // 秒杀接口
    @Test
    public void StringTest06(){
        BoundValueOperations stock = redisTemplate.boundValueOps("stock");
        if (stock.decrement() < 0 ){
            System.out.println("库存不足");
        } else {
            System.out.println("秒杀成功");
        }
    }
}

```

