package redis.RedisTest;

import cn.note.RedisNote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = RedisNote.class)
public class RedisNoteList {

    @Autowired
    RedisTemplate redisTemplate;


    //插入
    @Test
    public void ListTest(){
        BoundListOperations list = redisTemplate.boundListOps("list");
        for (int i = 0; i<10;i++){
            // 往尾部插入
            list.rightPush(i);
            // 往头部插入
            list.leftPush(i);
            //往尾部批量插入
            list.rightPushAll(1,2,3,4,5);
            //往尾部批量插入
            list.leftPushAll(1,2,3,4,5);
        }
    }

    //查询
    @Test
    public void ListTest2(){
        BoundListOperations list = redisTemplate.boundListOps("list");
        //范围获取
        System.out.println(list.range(0, list.size()));
        //根据下表获取某一个
        System.out.println(list.index(5));
    }

    //删除
    @Test
    public void ListTest3(){
        BoundListOperations list = redisTemplate.boundListOps("list");
        // 从头部删除  参数为删几个
        list.leftPop();
        // 从尾部删除指定数量
        list.rightPop();
        // 根据值进行删除
        list.remove(1,0);
        // 保留指定范围 ,其余都删掉
//        list.trim(1,2);
        list.set(1,10);
    }
}
