package zz.note.set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetMethod {
    public static void main(String[] args) {
        // 1. 以Set接口的实现类 HashSet_
        // 2. set 接口的实现类的对象(Set接口对象),不能存放重复的元素,可以添加一个null
        // 3. Set接口对象存放数据是无序(即添加的顺序和取出的顺序不一致)
        // 4. 注意: 取出的顺序是固定的,虽然不是添加的顺序
        Set set = new HashSet();
        set.add("john");
        set.add("lucy");
        set.add("john"); //重复数据
        set.add("jack");
        set.add(null);
        set.add(null);
        for (int i = 0; i < 10; i++) {
            System.out.println(set);
        }

        // 遍历
        // 1.迭代器
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object next =  iterator.next();
            System.out.println("迭代器"+next);
        }
        // 2.增强for
        for (Object o : set) {
            System.out.println(o);
        }

        // 3.forEach
        set.forEach( o -> {
            System.out.println(o);
        });

        // 不能通过索引来获取

    }
}
