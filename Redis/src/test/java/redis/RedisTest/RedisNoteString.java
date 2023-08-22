package redis.RedisTest;

import cn.note.RedisNote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = RedisNote.class)
public class RedisNoteString {
    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void StringTest(){
        // String类型
        BoundValueOperations username = redisTemplate.boundValueOps("username");
        //10秒过期
        username.set("徐庶",10, TimeUnit.SECONDS);


    }



    @Test
    public void StringTest01(){
        // String类型
        BoundValueOperations username = redisTemplate.boundValueOps("username");
        // 设置不存在的字符串 true=存入成功,false=存入失败
        System.out.println(username.setIfAbsent("徐庶"));
    }

    @Test
    public void StringTest02(){
        // 删除
        redisTemplate.delete("username");
    }


    @Test
    public void StringTest03(){
        //
        ValueOperations count = redisTemplate.opsForValue();
        // 累加 不传参就是 +1
        // 并发情况下,也可以保证线程安全
        String key = "count";
        count.increment("count",11L);  //累加
        count.decrement("count",10L);  //累减

    }


    /**
     * 秒杀 1小时
     * 预热 100库存
     */
    @Test
    public void StringTest05(){

        BoundValueOperations stock = redisTemplate.boundValueOps("stock");
        // 预热100个库存 1小时过期
        stock.set(100,1,TimeUnit.HOURS);
    }


    // 秒杀接口
    @Test
    public void StringTest06(){

        BoundValueOperations stock = redisTemplate.boundValueOps("stock");


        if (stock.decrement() < 0 ){
            System.out.println("库存不足");
        } else {
            System.out.println("秒杀成功");
        }
    }





}
