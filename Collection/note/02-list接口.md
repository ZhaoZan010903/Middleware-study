## List接口

List接口基本介绍:

List接口是Collection接口的子接口

1. List集合类中元素有序、且可重复
2. List集合中的每个元素都有其对应的顺序索引，即支持所以。
3. List容器中的元素都对应一个证书型的序号记载其在容器中的位置，可以根据序号存取容器中的元素。
4. JDK API中List接口的实现类有：

### 1. List常用方法

```java
public class ListMethod {
    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        list.add("张三丰");
        list.add("贾宝玉");


//         void add(int index,Object ele): 在index位置插入ele元素
//         void add(Object ele) : 在最后插入一个对象
        // 在 index = 1 的位置插入一个对象
        list.add(1, "jim"); //如果不写,则在最后的位置
        System.out.println(list);


//        boolean addAll(int index,Collection eles): 从index位置开始将eles中的所有元素添加进来
        List list1 = new ArrayList();
        list1.add("jack");
        list1.add("tom");
        list.addAll(1, list1);   //不写index则添加在最后的位置 ;
        System.out.println(list);

//         Object get(int index): 获取指定index位置的元素
        // 跳过


//        int indexOf(Object obj): 返回obj在集合中首次出现的位置
        System.out.println(list.indexOf("tom"));//2


//        int lastIndexOf(Object obj): 返回obj在当前集合中末次出现的位置
        System.out.println(list.lastIndexOf("jim"));


//        Object remove(int index) : 移除指定index位置的元素,并返回此元素
        list.remove(0);
        System.out.println(list);


//        Object set(int index, Object ele): 设置指定index位置的元素ele,相当于替换
        list.set(1, "玛丽");          // 如果给不存在的 则抛异常
        System.out.println(list);


//        List subList（int fromIndex，int toIndex): 返回从fromIndex到toIndex位置的子集合
        // 注意返回的子集合 [0,2);
        List list2 = list.subList(0, 2);
        System.out.println(list2);

    }
}
```

### 2.List的接口的三种遍历方式 
