package cn.note;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RocketMQRun {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQRun.class,args);
        log.debug("测试!");
    }
}
