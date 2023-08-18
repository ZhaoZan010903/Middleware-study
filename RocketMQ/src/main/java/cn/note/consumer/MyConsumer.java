package cn.note.consumer;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 */
@Component
@RocketMQMessageListener(topic = "Simple" ,consumerGroup = "my-boot-consumer-group",messageModel = MessageModel.CLUSTERING)
public class MyConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("收到了消息");
    }
}
