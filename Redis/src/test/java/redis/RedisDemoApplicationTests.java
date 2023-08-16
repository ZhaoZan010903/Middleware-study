package redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class RedisDemoApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        operations.set("name","测试");


//        redisTemplate.opsForHash().put("key:1","demo",list);
/*
        redisTemplate.opsForValue();
        redisTemplate.opsForList();
        redisTemplate.opsForSet();
        redisTemplate.opsForZSet();

        redisTemplate.opsForCluster();
        redisTemplate.opsForGeo();
        redisTemplate.opsForHyperLogLog();
        redisTemplate.opsForStream();
         */


    }

}
