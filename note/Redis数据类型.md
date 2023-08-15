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

