package zz.note.collection;

import java.util.ArrayList;

/**
 * @author ASUS
 */
public class CollectionMethod {
    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
//        add:添加元素
        list.add("Jack");
        list.add(10);        // list.add(new Integer(10));
        list.add(true);
        System.out.println("list=" + list);  //list=[Jack, 10, true]
//        remove:删除指定元素
        list.remove(1);  // 指定删除某个元素
        list.remove(true);   // 按索引删除,删除第一个元素
        System.out.println("list=" + list);
//        contains: 查找元素是否存在
        System.out.println(list.contains("Jack"));
//        size: 返回元素的个数
        System.out.println(list.size());
//        isEmpty: 判断是否为空
        System.out.println(list.isEmpty());
//        clear: 清空
        list.clear();
        System.out.println(list);
//        addAll:添加多个元素
        ArrayList list2 = new ArrayList();
        list2.add("红楼梦");
        list2.add("三国演义");
        list.addAll(list2);
        System.out.println(list);
//        containsAll: 查找多个元素是否都存在
        System.out.println(list.containsAll(list2)); //true
//        removeAll: 删除多个元素
        list.add("聊斋");
        list.removeAll(list2);
        System.out.println(list);    //聊斋


    }
}
