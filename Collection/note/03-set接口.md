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

### Set接口实现类-HashSet
* HashSet的全面说明
1. HashSet实现了Set接口
2. HashSet实际上是HashMap,下图
![img_1.png](images/img_1.png)
3. 