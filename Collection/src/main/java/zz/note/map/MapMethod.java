package zz.note.map;

import java.util.HashMap;
import java.util.Map;

public class MapMethod {
    public static void main(String[] args) {
        // 演示map接口常用方法
        Map map = new HashMap();

//         put:存入
        map.put("你好","你好");
        map.put("你好1","你好");
        map.put("Java","↓");
//        remove: 根据键删除映射关系
        map.remove("你好1");
//        get: 根据键获取值
        map.get("你好");
//        size: 获取有多少个键值对
        map.size();
//        isEmpty: 判断个数是否为0
        map.isEmpty();//false
//        clear: 清除k-v
//        map.clear();
//        containsKey: 查找键是否存在
        System.out.println(map.containsKey("Java")); //T
    }
}
