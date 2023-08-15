package cn.note.redis.pojo;

import cn.note.redis.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("redis")
@Data
public class User implements Serializable {
    @Id
    private Integer id;
    private String username;
}
