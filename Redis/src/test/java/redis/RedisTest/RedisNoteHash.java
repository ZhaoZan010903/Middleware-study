package redis.RedisTest;

import cn.note.RedisNote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest(classes = RedisNote.class)
public class RedisNoteHash {
    @Autowired
    private RedisTemplate redisTemplate;

    // 存入
    @Test
    public void HashTest() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        car.put("p_id", 1);
        car.put("p_total", 10);
    }

    // 获取map 所有
    @Test
    public void HashTest01() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        Map entries = car.entries();
        System.out.println(entries);
    }

    // 获取map某一项
    @Test
    public void HashTest02() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        System.out.println(car.get("p_total"));
    }

    //存入方法
    @Test
    public void HashTest03() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        //存
        car.put("p_id", 1);
        // 不存在则存入 成功返回true 不成功返回false
        Boolean pTotal = car.putIfAbsent("p_total", 10);
        System.out.println(pTotal);
    }


    // 删除
    @Test
    public void HashTest04() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        // 删除整个
        redisTemplate.delete("car");
        // 删除一项
        car.delete("p_id");
    }


    // 获取map的长度
    @Test
    public void HashTest05() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        System.out.println(car.size());
    }


    // 对map中的某个value进行增量 (原子)
    @Test
    public void HashTest06() {
        BoundHashOperations car = redisTemplate.boundHashOps("car");
        car.increment("p_total", 1);
    }


    /**
     * 电商购物车
     * 以用户id为key
     * 商品id为field
     * 商品数量为value
     */
    @Test
    public void Card(){
//                                                                  ↓ 那个用户的购物车
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
//                       ↓商品   ↓商品数量
        car.putIfAbsent("p_"+15,10);
        car.putIfAbsent("p_"+16,2);
        car.putIfAbsent("p_"+17,1);
    }

    /**
     * 增加购物车商品
     */
    @Test
    public void addCard(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        car.put("p_"+18,1);
    }


    /**
     * 增加购物车商品数量
     */
    @Test
    public void addCardNum(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        car.increment("p_"+18,1);
    }

    /**
     * 获取商品总数
     */
    @Test
    public void SumCard(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        Long size = car.size();
        System.out.println(size);
    }

    /**
     * 删除购物车商品
     */
    @Test
    public void deleteCard(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        Long delete = car.delete("p_" + 18);
        if(delete<=0){
            System.out.println("没有商品");
        }
    }

    /**
     * 获取购物车所有商品
     */
    @Test
    public void cardAll(){
        BoundHashOperations car = redisTemplate.boundHashOps("car" + 99);
        // 获取所有的商品id
        Set keys = car.keys();
        System.out.println(keys);
        // 获取所有商品的对应值以及数量
        Map entries = car.entries();
        System.out.println(entries);
        // 获取所有的value
        List values = car.values();
        System.out.println(values);
    }

}
