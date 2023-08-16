package cn.note.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 顺序消息的消费者
 */
public class OrderConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("SimpleConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        // *表示不过滤 所有消息都消费
        consumer.subscribe("Order", "*");
        consumer.setMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(i + "_消息消费成功_" + new String(list.get(i).getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        //并发消费
//        consumer.setMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
////                list.forEach(n -> System.out.println("消息消费成功"+list));
//                for (int i=0;i< list.size();i++){
//                    System.out.println(i+"_消息消费成功_"+new String(list.get(i).getBody()));
//                }
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
        consumer.start();
        System.out.println("消费者启动成功");
    }
}
