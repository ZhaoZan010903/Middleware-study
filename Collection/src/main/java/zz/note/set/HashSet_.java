package zz.note.set;

import java.util.HashSet;
import java.util.Set;

public class HashSet_ {
    public static void main(String[] args) {
        Set set = new HashSet();
        set.add("Java");
        set.add("Python");
        set.add("Go");
        set.add("Java");
        System.out.println(set);
    }
}
