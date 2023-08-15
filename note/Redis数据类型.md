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

___

#### 2.Hash
hash是一个键值(key => value)对集合.Redis hash是一个String类型的field和value的映射表,hash特别适合用于存储对象.(可以把value当做map)

```redis
HSET key field value -- 存储一个哈希表key的键值
HSETNX key field value -- 春促一个不存在的哈希表key的键值
HMSET key field value[field value ....] -- 在一个哈希表key中存储多个键值对(不支持)
HGET key field -- 获取哈希表key对应的field键值
HMGET key field [field ...] -- 批量获取哈希表key中多个field键值
HDEL key field [field ...] -- 删除哈希表key中的field键值
HLEN key -- 会返回哈希表key中field的数量
HGETALL key --返回哈希表key中所有键的键值
HINCRBY key field increement --为哈希表key中field键的值加上原子增量increment
```
**_使用场景:_** 存储部分变更数据,如用户登录信息、做购物车列表等。相对于String来说,对于对象存储,不用来回进行序列化,减少内存和CPU的消耗



```java
@SpringBootTest
public class RedisHash {
    @Autowired
    private RedisTemplate redisTemplate;

    // 存入
    @Test
    public void HashTest() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        car.put("p_id", 1);
        car.put("p_total", 10);

    }

    // 获取map 所有
    @Test
    public void HashTest01() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        Map entries = car.entries();
        System.out.println(entries);
    }

    // 获取map某一项
    @Test
    public void HashTest02() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        System.out.println(car.get("p_total"));
    }

    //存入方法
    @Test
    public void HashTest03() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        //存
        car.put("p_id", 1);
        // 不存在则存入 成功返回true 不成功返回false
        Boolean pTotal = car.putIfAbsent("p_total", 10);
        System.out.println(pTotal);
    }


    // 删除
    @Test
    public void HashTest04() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        // 删除整个
        redisTemplate.delete("car");
        // 删除一项
        car.delete("p_id");
    }


    // 获取map的长度
    @Test
    public void HashTest05() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        System.out.println(car.size());
    }


    // 对map中的某个value进行增量 (原子)
    @Test
    public void HashTest06() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        car.increment("p_total", 1);
    }


    /**
     * 电商购物车
     * 以用户id为key
     * 商品id为field
     * 商品数量为value
     */
    @Test
    public void Card(){
//                                                                  ↓ 那个用户的购物车
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
//                       ↓商品   ↓商品数量
        car.putIfAbsent("p_"+15,10);
        car.putIfAbsent("p_"+16,2);
        car.putIfAbsent("p_"+17,1);
    }

    /**
     * 增加购物车商品
     */
    @Test
    public void addCard(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        car.put("p_"+18,1);
    }


    /**
     * 增加购物车商品数量
     */
    @Test
    public void addCardNum(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        car.increment("p_"+18,1);
    }

    /**
     * 获取商品总数
     */
    @Test
    public void SumCard(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        Long size = car.size();
        System.out.println(size);
    }

    /**
     * 删除购物车商品
     */
    @Test
    public void deleteCard(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        Long delete = car.delete("p_" + 18);
        if(delete<=0){
            System.out.println("没有商品");
        }
    }

    /**
     * 获取购物车所有商品
     */
    @Test
    public void cardAll(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        // 获取所有的商品id
        Set keys = car.keys();
        System.out.println(keys);
        // 获取所有商品的对应值以及数量
        Map entries = car.entries();
        System.out.println(entries);
        // 获取所有的value
        List values = car.values();
        System.out.println(values);
    }

}
```

___




#### 3.list

list列表是简单的字符串列表,按照插入顺序排序.你可以添加一个元素到列表的头部(左边)或者尾部(右边).(相当于java的LinkedList(链表)和ArrayList)

```redis
LPUSH key value [value ....]  -- 将一个或多个值value插入到key列表的表头(最左边)
RPUSH key value [value ....]  -- 将一个或多个值value插入到key列表的表尾(最右边)
LPOP key -- 移除并返回key列表的头元素 
RPOP key -- 移除并返回key列表的尾元素
LRANGE key start stop -- 返回列表key中指定区间内的元素,区间以偏量start和stop指定
```

**_应用场景:_**Redis list的应用场景非常多,也是Redis最重要的数据结构之一,比如微博的关注列表,粉丝列表、热搜、top榜单等都可以用Redis的List结构来实现

```java
@SpringBootTest
public class RedisList {

    @Autowired
    RedisTemplate redisTemplate;
    
    //插入
    @Test
    public void ListTest(){
        BoundListOperations list = redisTemplate.boundListOps("list");
        for (int i = 0; i<10;i++){
            // 往尾部插入
            list.rightPush(i);
            // 往头部插入
            list.leftPush(i);
            //往尾部批量插入
            list.rightPushAll(1,2,3,4,5);
            //往尾部批量插入
            list.leftPushAll(1,2,3,4,5);
        }
    }

    //查询
    @Test
    public void ListTest2(){
        BoundListOperations list = redisTemplate.boundListOps("list");
        //范围获取
        System.out.println(list.range(0, list.size()));
        //根据下表获取某一个
        System.out.println(list.index(5));
    }

    //删除
    @Test
    public void ListTest3(){
        BoundListOperations list = redisTemplate.boundListOps("list");
        // 从头部删除  参数为删几个
        list.leftPop();
        // 从尾部删除指定数量
        list.rightPop();
        // 根据值进行删除
        list.remove(1,0);
        // 保留指定范围 ,其余都删掉
//        list.trim(1,2);
        list.set(1,10);
    }
}
```

___

#### 4.Set

set是S听类型的无序集合.不允许重复 
```redis
SADD key member [member ...] --往集合key中存入元素,元素存在则忽略,若key不存在则新建
SREM key member [member ...] --从集合key中删除元素
SCARD key --获取集合key中所有元素
SISMEMBER key member -- 判断member元素是否存在于集合key中
SRANDMEMBER key [count] -- 从集合key中选出count个随机元素,元素不从key中删除
SPOP key [count] -- 从集合key中选出count个元素,元素从key中删除

Set运算操作
SINTER  key [key] --交集运算
SINTERSTORE destination key [key ...]  --将交集结果存入新集合 destination中
SUNION key [key ...] --并集运算
SDIFF key [key ...] --差集运算
SDIFFSTORE destination key [key...] --差集结果存入新集合destination中
```

**_使用场景:_** 共同关注的人,可能认识的人;

```java
@SpringBootTest
public class RedisSet {

    @Autowired
    private RedisTemplate redisTemplate;

    //添加
    @Test
    public void SetTest() {
        BoundSetOperations set = redisTemplate.boundSetOps("set1");
        set.add(1, 2, 3, 4, 5, 6, 1, 312, 12, 7, 8, 9);
    }
    
    // 获取,删除
    @Test
    public void SetTest01() {
        BoundSetOperations set = redisTemplate.boundSetOps("set");
        //获取整个set set.members()
        System.out.println(set.members());
        //获取个数 set.size()
        System.out.println(set.size());
        //根据摸个元素判断是否在set中  set.isMember(1)
        System.out.println(set.isMember(1));
        //根据count获取随机元素
        //                                    ↓ 获取个数
        System.out.println(set.randomMembers(2));
        System.out.println(set.randomMembers(2));
        System.out.println(set.randomMember());
        //带删除
        System.out.println(set.pop()); //带

        SetOperations setOperations = redisTemplate.opsForSet();
        System.out.println(setOperations.pop("set", 2));
    }

    // Set运算

    //交集
    @Test
    public void SetSum() {
        BoundSetOperations set = redisTemplate.boundSetOps("set");
        // 取交集
        System.out.println(set.intersect("set1"));
        //取出交集并存入新的set中
        set.intersectAndStore("set1", "set3");
        BoundSetOperations set3 = redisTemplate.boundSetOps("set3");
        System.out.println(set3.members());
    }


    //并集
    @Test
    public void SetSum02() {
        BoundSetOperations set = redisTemplate.boundSetOps("set");

        System.out.println(set.union("set1"));
    }


    //差集
    @Test
    public void SetSum03() {
        BoundSetOperations set = redisTemplate.boundSetOps("set1");
        System.out.println(set.diff("set"));
    }
}
```

___

#### 5.Zset
Zset与set类似,区别是set是无序的,sorted set是有序的并且不重复的集合列表,可以通过用户额外提供一个优先级的参数来为成员排序,并且是插入有序的,即自动排序
```redis
ZADD key score member [[score member]...]
```