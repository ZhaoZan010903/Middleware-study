package redis.redisRepository;

import cn.note.RedisNote;
import cn.note.pojo.Account;
import cn.note.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RedisNote.class)
public class accountRepositoryImpl {

    @Autowired
    AccountRepository AccountRepository;

    @Test
    public void testAdd(){
        Account account = new Account();
        account.setId(1);
        account.setUsername("徐");
        AccountRepository.save(account);
    }

    @Test
    public void testSelect(){
        System.out.println(AccountRepository.findById(1));
    }

    @Test
    public void testDel(){
        AccountRepository.deleteById(1);
    }


}
