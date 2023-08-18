package cn.note.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.Serializable;

/**
 * @author ASUS
 */
@Configuration
@EnableTransactionManagement    //开启事务
@ComponentScan("cn.note")
@EnableRedisRepositories("cn.note.repository")
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(factory);
        // 只能传入String类型的值 序列化的原因 只能传入String类型的值(存入Redis)
//        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        // 改成JSON的序列化器
        //
        redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return redisTemplate;
    }

    //Redis声明式事务
    @Bean
    public RedisTemplate tranRedisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(factory);
        // 只能传入String类型的值 序列化的原因 只能传入String类型的值(存入Redis)
//        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        // 改成JSON的序列化器
        //
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));

        // 支持Spring声明式事务
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }


}
