package redis.RedisTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SpringBootTest
public class RedisZset {
    @Autowired
    private RedisTemplate redisTemplate;

    // 添加
    @Test
    public void ZsetAdd() {
        BoundZSetOperations zset = redisTemplate.boundZSetOps("zset");
        zset.add("张三", 100);

        Set<ZSetOperations.TypedTuple> set = new HashSet<>();
        set.add(ZSetOperations.TypedTuple.of("李四", Double.valueOf("80")));
        set.add(ZSetOperations.TypedTuple.of("王五", Double.valueOf("70")));
        set.add(ZSetOperations.TypedTuple.of("赵六", Double.valueOf("60")));
        zset.add(set);
    }

    // 获取
    @Test
    public void ZsetGet() {
        BoundZSetOperations zset = redisTemplate.boundZSetOps("zset");
        // 获取分数
        System.out.println(zset.score("张三", "李四"));
        // 个数
        System.out.println(zset.size());
        // 原子添加并返回
        System.out.println(zset.incrementScore("李四", 1));
    }

    // 范围查询
    @Test
    public void testGetRange() {
        BoundZSetOperations zset = redisTemplate.boundZSetOps("zset");
        // 范围升序查询

        //升序顺序范围
        System.out.println(zset.range(0, 2));
        // 分数查询
        System.out.println(zset.rangeByScore(60, 90));
        // 带分数
        System.out.println(zset.rangeWithScores(1, 2));
        System.out.println(zset.rangeByScoreWithScores(60, 90));


        // 范围降序查询

        //升序降序范围
        System.out.println(zset.reverseRange(0, 2));
        // 分数查询
        System.out.println(zset.reverseRangeByScore(60, 90));
        // 带分数
        System.out.println(zset.reverseRangeWithScores(1, 2));
        System.out.println(zset.reverseRangeByScoreWithScores(60, 90));
    }


    //删除
    @Test
    public void ZsetDel() {
        BoundZSetOperations zset = redisTemplate.boundZSetOps("zset");
        System.out.println(zset.remove("张三"));
        //按照升序范围移除
        zset.removeRange(0, 1);
        zset.removeRangeByScore(100, 90); //按照分数
    }

    @Test
    public void test() {
        BoundZSetOperations zset = redisTemplate.boundZSetOps("news20000101");
        //
        zset.incrementScore("守护香港:1", 1);
        System.out.println(zset.reverseRangeWithScores(0, 9));
    }
}
