package zz.note.list;

import java.util.ArrayList;

public class List {
    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        // 1. List集合类中元素有序(即添加顺序和取出顺序一致)且可重复
        list.add("jack");
        list.add("tom");
        list.add("mary");
        list.add("hsp");
        list.add("tom");
        System.out.println("list = "+list);
        // 2. List集合中的每个元素都有对应的顺序所以,即支持所以
        // 索引是从0开始
        System.out.println(list.get(3)); //hsp

        // 3.


    }
}
