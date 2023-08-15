package cn.note.redis.RedisTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisString {
    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void StringTest(){
        // String类型
        BoundValueOperations username = redisTemplate.boundValueOps("username");
        //10秒过期
        username.set("徐庶",10, TimeUnit.SECONDS);


    }




}
