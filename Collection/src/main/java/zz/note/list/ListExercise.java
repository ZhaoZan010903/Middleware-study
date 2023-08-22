package zz.note.list;

import java.util.ArrayList;
import java.util.List;

public class ListExercise {
    public static void main(String[] args) {

        List list = new ArrayList();
        list.add(new Book("红楼梦", "曹雪芹", 100));
        list.add(new Book("西游记", "吴承恩", 10));
        list.add(new Book("水浒传", "施耐庵", 9));
        list.add(new Book("三国演义", "罗贯中", 80));

        // 对集合进行排序

        for (Object o : list) {
            System.out.println(o);
        }

        // 冒泡排序
        sort(list);

        System.out.println("======排序后======");

        list.forEach(a -> {
            System.out.println(a);
        });
    }

    public static void sort(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                Book o1 = (Book) list.get(j);
                Book o2 = (Book) list.get(j + 1);
                if (o1.getPrice() > o2.getPrice()) {
                    list.set(j, o2);
                    list.set(j + 1, o1);
                }

            }
        }
    }

}
