package cn.note.RocketMQ.schedule;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 预定日程生产者
 */
public class ScheduleProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("ScheduleProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Simple", "Tags", (i + "ScheduleProducer").getBytes(StandardCharsets.UTF_8));

            // 延时设置 等级
//            message.setDelayTimeLevel(3);

            //延时设置 10秒之后发送 最多3天
            message.setDelayTimeMs(10000L);

            SendResult send = producer.send(message);
            System.out.println(i + "消息发送成功" + send + LocalDateTime.now());
        }
        producer.shutdown();
    }
}
