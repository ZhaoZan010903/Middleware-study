package zz.note.collection;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 使用Iterator迭代器
 * @author ASUS
 */
public class CollectionIterator {
    public static void main(String[] args) {
        Collection col = new ArrayList();

        col.add(new Book("三国演义", "罗贯中", 10.1));
        col.add(new Book("小李飞刀", "古龙", 10.1));
        col.add(new Book("红楼梦", "曹雪芹", 10.1));

//        System.out.println("col="+col);
//        col=[Book(name=三国演义, author=罗贯中, price=10.1), Book(name=小李飞刀, author=古龙, price=10.1), Book(name=红楼梦, author=曹雪芹, price=10.1)]
//        使用迭代器遍历遍历 col集合
        //1.先得到 col 对应的 迭代器
        Iterator iterator = col.iterator();
        //2. 使用while循环遍历            // 快捷键 itit            // 使用 Ctrl + j
        while (iterator.hasNext()) {   // 判断是否还有数据
            //返回下一个元素,类型是Object
            Object next = iterator.next();
            System.out.println("next=" + next);
        }
        //3. 当退出while循环后 , 这是iterator迭代器,指向最后的元素
//        iterator.next(); //NoSuchElementException
        //4. 如果希再次遍历,需要重置迭代器;
        iterator = col.iterator();
        System.out.println("======");
        while (iterator.hasNext()) {
            Object next =  iterator.next();
            System.out.println(next);
        }
    }
}

@Data
class Book {

    private String name;
    private String author;
    private double price;

    public Book(String name, String author, double price) {
        this.name = name;
        this.author = author;
        this.price = price;
    }

}
