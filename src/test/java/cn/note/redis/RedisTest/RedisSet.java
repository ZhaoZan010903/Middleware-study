package cn.note.redis.RedisTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

@SpringBootTest
public class RedisSet {

    @Autowired
    private RedisTemplate redisTemplate;

    //添加
    @Test
    public void SetTest() {
        BoundSetOperations set = redisTemplate.boundSetOps("set1");
        set.add(1, 2, 3, 4, 5, 6, 1, 312, 12, 7, 8, 9);
    }

    // 获取,删除
    @Test
    public void SetTest01() {
        BoundSetOperations set = redisTemplate.boundSetOps("set");
        //获取整个set set.members()
        System.out.println(set.members());
        //获取个数 set.size()
        System.out.println(set.size());
        //根据摸个元素判断是否在set中  set.isMember(1)
        System.out.println(set.isMember(1));
        //根据count获取随机元素
        //                                    ↓ 获取个数
        System.out.println(set.randomMembers(2));
        System.out.println(set.randomMembers(2));
        System.out.println(set.randomMember());
        //带删除
        System.out.println(set.pop()); //带

        SetOperations setOperations = redisTemplate.opsForSet();
        System.out.println(setOperations.pop("set", 2));
    }

    // Set运算

    //交集
    @Test
    public void SetSum() {
        BoundSetOperations set = redisTemplate.boundSetOps("set");
        // 取交集
        System.out.println(set.intersect("set1"));
        //取出交集并存入新的set中
        set.intersectAndStore("set1", "set3");
        BoundSetOperations set3 = redisTemplate.boundSetOps("set3");
        System.out.println(set3.members());
    }


    //并集
    @Test
    public void SetSum02() {
        BoundSetOperations set = redisTemplate.boundSetOps("set");

        System.out.println(set.union("set1"));
    }


    //差集
    @Test
    public void SetSum03() {
        BoundSetOperations set = redisTemplate.boundSetOps("set1");
        System.out.println(set.diff("set"));
    }
}
