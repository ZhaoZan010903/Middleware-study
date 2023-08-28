package zz.note.set;

import java.util.HashSet;

public class HashSet01 {
    public static void main(String[] args) {
        HashSet set = new HashSet();
        // 1. 在执行add'方法后,会返回一个boolean值
        // 2. 如果添加成功,返回true,否则返回false
        // 3. 可以通过remove 指定删除哪个对象
        System.out.println(set.add("john")); // T
        System.out.println(set.add("lucy")); // T
        System.out.println(set.add("john")); // F
        System.out.println(set.add("jack")); // T
        System.out.println(set.add("Rose")); // T

        boolean john = set.remove("john");

        System.out.println(set);        // [Rose, lucy, jack]

        set = new HashSet<>();
        // 4. HashSet 不能添加相同的元素
        set.add("lucy");
        set.add("lucy"); // 加入不了

        set.add(new Dog("tom")); //可以加入
        set.add(new Dog("tom")); //可以加入
        System.out.println(set);  //[Dog{name='tom'}, Dog{name='tom'}, lucy]

        // new String
        // 看源码,做分析 ;
        set.add("jack"); // Ok
        set.add("jack"); //加入不了
        System.out.println(set); //[Dog{name='tom'}, Dog{name='tom'}, lucy, jack]

    }
}

class Dog {
    private final String name;

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
