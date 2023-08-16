package cn.note.simple;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 拉模式 - 指定获取一个queue消息
 */
public class LitePullAssign {
    public static void main(String[] args) throws MQClientException {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("LitePullConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.start();
        Collection<MessageQueue> simple = consumer.fetchMessageQueues("Simple");
        ArrayList<MessageQueue> messageQueues = new ArrayList<>(simple);
        consumer.assign(messageQueues);
        consumer.seek(messageQueues.get(1),10);
        while (true){
            List<MessageExt> poll = consumer.poll();
            System.out.println("消息拉去成功");
            poll.forEach( n->{
                System.out.println( "消费成功_" + n );
            });
        }
    }
}
