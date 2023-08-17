package cn.note.RocketMQ.simple;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 拉模式 - 随机获取一个queue消息
 */
public class LitePull {
    public static void main(String[] args) throws MQClientException {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("LitePullConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.subscribe("Simple", "*");
        consumer.start();
        while (true) {
            List<MessageExt> messageExts = consumer.poll();
            System.out.println("消息拉去成功");
            messageExts.forEach(n->{
                System.out.println("消息消费成功"+n);
            });
        }
    }
}
