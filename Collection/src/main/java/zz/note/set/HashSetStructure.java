package zz.note.set;

/**
 * 模拟一个HashSet的底层
 */
public class HashSetStructure {
    public static void main(String[] args) {
        // HashSet的底层就是HashMap

        // 1. 创建一个数组, 数组的类型是 Node[]
        // 2. 有些人,直接把 Node[] 数组称为 表 (Table)
        Node[] table = new Node[16];
        // 3. 创建结点
        Node john = new Node("john", null);
        table[2] = john;
        Node jack = new Node("jack", null);
        john.next=jack;   // 将jack 结点挂载到john
        Node rose = new Node("Rose", null);
        jack.next = rose; // 将rose结点挂载到jack
        Node lucy = new Node("lucy", null);
        table[3] = lucy;  // 将lucy放到 table索引为3的位置;
        System.out.println("table"+table);
    }
}
class Node {  // 结点,存储数据,可以指向下一个结点,从而星城链表
    Object item; // 存放数据
    Node next; // 指向下一个结点

    public Node(Object item, Node next) {
        this.item = item;
        this.next = next;
    }
}
