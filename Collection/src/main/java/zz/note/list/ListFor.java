package zz.note.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * 遍历List
 */
public class ListFor {
    public static void main(String[] args) {
//        List list = new Vector();
//        List list = new Vector();
        List list = new ArrayList();
        list.add("jack");
        list.add("tom");
        list.add("鱼香肉丝");
        list.add("北京烤鸭");

        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            System.out.println(next);
        }

        System.out.println("====增强for====");
        // 2.增强for
        for (Object o : list) {
            System.out.println(o);
        }

        // 3.forEach
        list.forEach( o -> {
            System.out.println(o);
        });


    }
}
