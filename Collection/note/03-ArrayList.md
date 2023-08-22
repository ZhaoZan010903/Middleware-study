## ArrayList

### 1.ArrayList注意事项
* permits all elements,including null,ArrayList可以加入null,并且多个
* ArrayList 是由数组来实现数据存储的
* ArrayList 基本等同于Vector,除了ArrayList是线程不安全(执行效率高),在多线程情况下,不建议使用ArrayList

### 2.ArrayList底层结构和源码分析

1. ArrayList中维护了一个Object类型的数组elementData
2. 当创建ArrayList对象时,如果使用的是无参构造器,则初始elementData容量为0,第1次添加,则扩容elementData为10,如需要再次扩容,则扩容elementData为1.5倍
3. 如果使用的是指定大小的构造器,则初始elementData容量为指定大小,如果需要扩容,则扩容elementData为1.5倍