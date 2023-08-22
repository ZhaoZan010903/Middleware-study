package zz.note.list.linkedlist;

import java.util.LinkedList;

public class LinkedListCRUD {
    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.add(1);
        list.add(2);
        System.out.println(list);
        /*
        1. LinkedList list = new LinkedList();
           public LinkedList(){}
        2. 这时list的属性 first = null last = null
        3. 执行
            public boolean add(E e){
                  linkLast(e);
                  return true;
            }
         4.
         */
    }
}
