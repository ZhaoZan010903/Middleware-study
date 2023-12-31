# Set接口基本介绍

1. 无序(添加和取出的顺序不一致),没有索引
2. 不允许重复元素,所以最多包含一个null
3. JDK API中Set接口的实现类有:
   ![img.png](images/img_3.png)

## Set接口和常用方法

* Set接口的常用方法
  和List接口一样，Set接口也是Collection的子接口，因此，常用方法和Collection接口一样
* Set接口的遍历方式

1. 迭代器
2. 增强for
3. 不能使用索引的方式来获取

## Set接口实现类-HashSet

* HashSet的全面说明

1. HashSet实现了Set接口
2. HashSet实际上是HashMap,下图
   ![img_1.png](images/img_1.png)
3. 可以存放null值,但是只能有一个null
4. HashSet不保证元素是有序的,取决于hash后,再确定索引的结果.(即,不保证存放元素的顺序和取出顺序一致)
5. 不能有重复元素/对象.

#### 1.HashSet的案例

下面这段代码在添加一个重复的元素时会返回false,

输出时是无序的;

代码:

```java
public class HashSet01 {
    public static void main(String[] args) {
        HashSet set = new HashSet();
        // 1. 在执行add'方法后,会返回一个boolean值
        // 2. 如果添加成功,返回true,否则返回false
        // 3. 可以通过remove 指定删除哪个对象
        System.out.println(set.add("john")); // T
        System.out.println(set.add("lucy")); // T
        System.out.println(set.add("john")); // F
        System.out.println(set.add("jack")); // T
        System.out.println(set.add("Rose")); // T

        boolean john = set.remove("john");

        System.out.println(set);        // [Rose, lucy, jack]

        set = new HashSet<>();
        // 4. HashSet 不能添加相同的元素
        set.add("lucy");
        set.add("lucy"); // 加入不了

        set.add(new Dog("tom")); //可以加入
        set.add(new Dog("tom")); //可以加入
        System.out.println(set);  //[Dog{name='tom'}, Dog{name='tom'}, lucy]

        // new String
        // 看源码,做分析 ;
        set.add(new String("jack")); // Ok
        set.add(new String("jack")); //加入不了
        System.out.println(set); //[Dog{name='tom'}, Dog{name='tom'}, lucy, jack]

    }
}

class Dog {
    private String name;

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
```
### 2.HashSet的底层原理

* HashSet底层机制说明
  * 分析HashSet底层是HashMap,HashMap底层是(数组+链表+红黑树);

```java
/**
 * 模拟一个HashSet的底层
 */
public class HashSetStructure {
    public static void main(String[] args) {
        // HashSet的底层就是HashMap

        // 1. 创建一个数组, 数组的类型是 Node[]
        // 2. 有些人,直接把 Node[] 数组称为 表 (Table)
        Node[] table = new Node[16];
        // 3. 创建结点
        Node john = new Node("john", null);
        table[2] = john;
        Node jack = new Node("jack", null);
        john.next=jack;   // 将jack 结点挂载到john
        Node rose = new Node("Rose", null);
        jack.next = rose; // 将rose结点挂载到jack
        Node lucy = new Node("lucy", null);
        table[3] = lucy;  // 将lucy放到 table索引为3的位置;
        System.out.println("table"+table);
    }
}
class Node {  // 结点,存储数据,可以指向下一个结点,从而星城链表
    Object item; // 存放数据
    Node next; // 指向下一个结点

    public Node(Object item, Node next) {
        this.item = item;
        this.next = next;
    }
}
```
数组+链表 结构
![img_2.png](images/img_2.png)

1. HashSet底层是HashMap
2. 添加一个元素时,先得到hash值 
3. 找到存储数据表table,看这个索引位置是否已经存放的有元素
4. 如果没有,直接加入
5. 如果有,调用equals比较,如果相同,就放弃添加,如果不想同,则添加到最后
6. 在Java8中,如果一条链表的元素个数超过 TREEIFY_THRESHOLD(默认是8),并且table的大小 >= MIN_TREEIFY_CAPACITY(默认64),就会进行树化(红黑树)


#### 2.1 HashSet底层机制说明
分析HashSet的添加元素底层是如何实现(hash()+equals())

```java
       /* 
        HashSet 的源码解读
        1. 执行构造器 HashSet()
           public HashSet() {
                map = new HashMap<>();
            }
        
        2. 执行 add()
          public boolean add(E e) {
                return map.put(e, PRESENT)==null;   // (static) PRESENT = new Object();
            }
        
         3. 执行 put() , 该方法会执行 hash(key) 得到key对应的hash值 算法 h = key.hashCode() ^ (h >>> 16)
             public V put(K key, V value) {     // key = 是存入的东西
                return putVal(hash(key), key, value, false, true);
            }
        
         4. 执行 putVal()
             final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                           boolean evict) {
                Node<K,V>[] tab; Node<K,V> p; int n, i;  // 定义了辅助变量
                // table 就是 HashMap 的一个数组,类型是 Node[]
                // if 语句表示如果当前table 是null, 或者 大小=0
                // 就是第一次扩容, 到16个空间
                if ((tab = table) == null || (n = tab.length) == 0)
                    n = (tab = resize()).length;
                // (1)根据key, 得到hash 去计算该key应该存放到table表的哪个索引位置
                // 并把这个位置的对象,赋给变量P
                // (2)判断P 是否为null
                // (2.1) 如果P为null,表示还没有存放元素, 就创建一个Node (key = "java" , value = PRESENT)
                // (2.2) 就放在该位置 tab[i] = newNode(hash,key,value,null)
                if ((p = tab[i = (n - 1) & hash]) == null)
                    tab[i] = newNode(hash, key, value, null);
                else {
                    Node<K,V> e; K k;
                    if (p.hash == hash &&
                        ((k = p.key) == key || (key != null && key.equals(k))))
                        e = p;
                    else if (p instanceof TreeNode)
                        e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
                    else {
                        for (int binCount = 0; ; ++binCount) {
                            if ((e = p.next) == null) {
                                p.next = newNode(hash, key, value, null);
                                if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                                    treeifyBin(tab, hash);
                                break;
                            }
                            if (e.hash == hash &&
                                ((k = e.key) == key || (key != null && key.equals(k))))
                                break;
                            p = e;
                        }
                    }
                    if (e != null) { // existing mapping for key
                        V oldValue = e.value;
                        if (!onlyIfAbsent || oldValue == null)
                            e.value = value;
                        afterNodeAccess(e);
                        return oldValue;
                    }
                }
                ++modCount;
                if (++size > threshold)
                    resize();
                afterNodeInsertion(evict);
                return null;
            }
            
        */
```
