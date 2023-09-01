## Map接口和常用方法

* Map接口实现类的特点
  **注意**: 这里说的是JDK8的Map接口特点

1. Map与Collection并列存在.用于保存具有映射关系的数据: Key-Value
2. Map中的Key和value可以是任何引用类型的数据,会封装到HashMap$Node对象中
3. Map中的key不允许重复,愿意和HashSet一样,前面分析过源码
4. Map中的value可以重复
5. Map的key可以为null,value也可以为null,注意key为null,只能有一个,value为null,可以多个
6. 常用String类型作为Map的key
7. key和value之间存在单向一对一关系,即通过指定的key总能找到对应的value
8. Map存放数据的key-value示意图,一对k-v是存放在一个Node中的,有因为Node实现了Entry接口,有些书上也说 一对k-v就是一个Entry
   ![img_4.png](images/img_4.png)

### 1.Map接口的常用方法

```java
public class MapMethod {
    public static void main(String[] args) {
        // 演示map接口常用方法
        Map map = new HashMap();

//         put:存入
        map.put("你好", "你好");
        map.put("你好1", "你好");
        map.put("Java", "↓");
//        remove: 根据键删除映射关系
        map.remove("你好1");
//        get: 根据键获取值
        map.get("你好");
//        size: 获取有多少个键值对
        map.size();
//        isEmpty: 判断个数是否为0
        map.isEmpty();//false
//        clear: 清除k-v

//        map.clear();

//        containsKey: 查找键是否存在
        System.out.println(map.containsKey("Java")); //T
    }
}
```

### 2.Map的遍历方式

```java
public class MapFor {
    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("Java", "↑");
        map.put("GO", "^");
        map.put("HTML", "&");
        map.put("Python", "*");
        map.put("CSS", "↓");

        //第一组: 先取出 所有的Key, 通过Key 取出对应的Value
        Set keyset = map.keySet();
        System.out.println(keyset);

        // (1) 增强for循环
        System.out.println("增强for");
        for (Object key : keyset) {
            System.out.println(key + " <-> " + map.get(key));
        }

        // (2) 迭代器
        System.out.println("迭代器");
        Iterator iterator = keyset.iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            System.out.println(key + " <-> " + map.get(key));
        }

        //第二组:把所有的values取出
        Collection values = map.values();
        System.out.println(values);

        //(1) 增强for循环
        System.out.println("=====增强for====");
        for (Object value : values) {
            System.out.println(value);
        }

        //(2) 迭代器
        System.out.println("====迭代器=====");
        Iterator iterator1 = values.iterator();
        while (iterator1.hasNext()) {
            Object next = iterator1.next();
            System.out.println(next);
        }

        //第三组: 通过EntrySet
        Set set = map.entrySet();
        //(1) 增强for
        for (Object o : set) {
            // 将entry 转成 Map.Entry
            Map.Entry m = (Map.Entry) o;
            System.out.println(m.getKey() + "---" + m.getValue());
        }
        //(2) 迭代器
        Iterator iterator2 = set.iterator();
        while (iterator2.hasNext()) {
            Object next = iterator2.next();
            System.out.println(next.getClass()); //HashMap$Node -实现-> Map.Entry (getKey,getValue)
            System.out.println(next);
        }

    }
}
```