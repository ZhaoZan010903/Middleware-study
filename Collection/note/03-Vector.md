## Vector

### 1.Vector基本介绍
1. Vector类的定义说明
```java
public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```
2. Vector底层也是一个对象数组,protected Object[] elementData;
3. Vector是线程同步的,即线程安全,Vector类的操作方法带有*synchronized*
4. 在开发中,需要线程同步安全时,考虑使用Vector
5. 如果是无参,默认10,满后,就按2倍扩容.如果指定大小,则每次直接按2倍扩

