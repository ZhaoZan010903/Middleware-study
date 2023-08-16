package cn.note.simple;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 异步发送
 */
public class AsyncProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("AsyncProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            final int index = i;
            System.out.println(i);
            Message message = new Message("Simple", "Tags", (i + "_SyncProducer").getBytes(StandardCharsets.UTF_8));
            /*
            1.发送消息没有返回值
            提供了回调方法
             */
            producer.send(message, new SendCallback() {

                //发送成功
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.println(index + "消息发送成功_" + sendResult);
                }

                //发送失败
                @Override
                public void onException(Throwable throwable) {
                    countDownLatch.countDown();
                    System.out.println(index + "消息发送成功_" + throwable);
                }
            });
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }
}
