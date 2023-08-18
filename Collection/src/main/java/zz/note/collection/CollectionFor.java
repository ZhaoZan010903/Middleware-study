package zz.note.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 增强for循环
 */
public class CollectionFor {
    public static void main(String[] args) {
        Collection col = new ArrayList();

        col.add(new Book("三国演义", "罗贯中", 10.1));
        col.add(new Book("小李飞刀", "古龙", 10.1));
        col.add(new Book("红楼梦", "曹雪芹", 10.1));


        //使用增强for
        // 1.使用增强for,在Collection集合
        // 2.增强for, 底层仍然是迭代器
        // 3.增强for可以理解成简化版本的迭代器遍历
        // 4.快捷方式 I .for
        for (Object o : col) {
            System.out.println(o);
        }

        col.forEach( o -> {
            System.out.println(o);
        });
        // 增强for也可以直接在数组上使用
    }
}
