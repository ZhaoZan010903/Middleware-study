import cn.note.RocketMQRun;
import cn.note.producer.MyProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RocketMQRun.class)
public class RocketMQ {

    @Autowired
    private MyProducer producer;

    @Test
    void testSendMessage(){
        String topic = "my-boot-topic";
        String message = " hello ";
        producer.sendMessage(topic,message);
        System.out.println("消息发送成功");
    }

}
