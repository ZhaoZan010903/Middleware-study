package redis;

import cn.note.redis.pojo.User;
import cn.note.redis.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
public class redisRepository {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testAdd(){
        User user = new User();
        user.setId(1);
        user.setUsername("徐");
        userRepository.save(user);
    }

    @Test
    public void testSelect(){
        System.out.println(userRepository.findById(1));
    }

    @Test
    public void testDel(){
        userRepository.deleteById(1);
    }

}
