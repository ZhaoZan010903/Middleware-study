package redis.redisTransaction;

import cn.note.RedisNote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = RedisNote.class)
public class RedisNoteTransactionTest {

    @Autowired
    RedisTemplate redisTemplate;


    @Autowired
    RedisTemplate tranRedisTemplate;

    //@Autowired会根据类型自动注入,如果找到了两个则会根据名字


    // execute
    @Test
    public void testExecute() {
        // 一旦出现异常 中断主线程, 因为它是同步的 解决方法:加上try-catch
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 如果在其他线程中发现了改变,将回滚事物
                operations.watch("execute");

                operations.multi(); // 开启事务

                // 无需在这里try 回滚事物 ,出现异常底层会自动帮你回滚
                operations.opsForValue().set("execute", "test");
                // 数据隔离性,事物没有提交是查不到的
                System.out.println(operations.opsForValue().get("execute"));

//                int a = 1/0;
                return operations.exec();
            }
        });

        // 上面redis事物出现了异常,下面业务正常执行
        System.out.println("业务异常");
        // TODO: 执行了业务操作
    }


    @Test
    public void testWatchExecute() {
        redisTemplate.opsForValue().set("execute", "test1");
    }


    @Test
    @Transactional
    @Commit
    public void testTransactional() {
        tranRedisTemplate.opsForValue().set("tran", "test11");
        System.out.println(tranRedisTemplate.opsForValue().get("tran"));
    }


}
