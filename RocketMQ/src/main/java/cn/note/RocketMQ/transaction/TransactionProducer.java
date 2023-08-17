package cn.note.RocketMQ.transaction;


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 事务消息生产者
 */
public class TransactionProducer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        TransactionMQProducer producer = new TransactionMQProducer("transactionProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        // 异步提交提升事物状态,提升性能
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("ExecutorService-");
                return thread;
            }
        });
        // 使用异步提交
        producer.setExecutorService(threadPoolExecutor);
        // 本地事务监听器
        producer.setTransactionListener(new TransactionListenerImpl());
        producer.start();
        String[] tags = new String[]{"TagA", "TagB", "TagC","TagD","TagE"};
        for (int i = 0;i < 10; i++){
            Message message = new Message("Transaction",
                    tags[i % tags.length],
                    (tags[i % tags.length]+"_TransactionProducer").getBytes(StandardCharsets.UTF_8));
            TransactionSendResult transactionSendResult = producer.sendMessageInTransaction(message, null);
            System.out.println("消息发送成功_" + transactionSendResult);
            Thread.sleep(10);
        }
        Thread.sleep(100000);
        producer.shutdown();
    }
}
