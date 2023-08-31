package zz.note.map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapSource_ {
    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("no1","赵錾");
        map.put("no2","张无忌");

        Set set = map.entrySet();
        System.out.println(set.getClass()); //HashMap$EntrySet



        for (Object entry : set) {

        }
    }
}
