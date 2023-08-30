package zz.note.map;

import java.util.HashMap;
import java.util.Map;

public class Map_ {
    public static void main(String[] args) {
        // 1. Map与Collection并列存在.用于保存具有映射关系的数据: Key-Value(双列元素)
        // 2. Map中的Key和value可以是任何引用类型的数据,会封装到HashMap$Node对象中
        // 3. Map中的key不允许重复,愿意和HashSet一样,前面分析过源码
        Map map = new HashMap<>();
        map.put("no1","赵錾");
        map.put("no2","张无忌");
        map.put("no1","123"); // 遇到相同的key会替换value
        map.put(null,"1234");
        map.put(null,"1234");
        map.put("123",null);
        map.put("1234",null);


        System.out.println(map);
        map.forEach( (i,a) -> System.out.println(i));
        map.get("no1");
    }
}
