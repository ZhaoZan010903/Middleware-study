package cn.note.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author ASUS
 */
@RedisHash("redis")
@Data
public class Account {
    @Id
    private Integer id;
    private String username;
}
