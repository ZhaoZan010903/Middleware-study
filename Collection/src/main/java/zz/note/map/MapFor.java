package zz.note.map;

import java.util.*;

public class MapFor {
    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("Java","↑");
        map.put("GO","^");
        map.put("HTML","&");
        map.put("Python","*");
        map.put("CSS","↓");

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
            Object next =  iterator1.next();
            System.out.println(next);
        }

        //第三组: 通过EntrySet
        Set set = map.entrySet();
        //(1) 增强for
        for (Object o : set) {
            // 将entry 转成 Map.Entry
            Map.Entry m = (Map.Entry) o;
            System.out.println(m.getKey()+"---"+m.getValue());
        }
        //(2) 迭代器
        Iterator iterator2 = set.iterator();
        while (iterator2.hasNext()) {
            Object next =  iterator2.next();
            System.out.println(next.getClass()); //HashMap$Node -实现-> Map.Entry (getKey,getValue)
            System.out.println(next);

        }


    }
}
