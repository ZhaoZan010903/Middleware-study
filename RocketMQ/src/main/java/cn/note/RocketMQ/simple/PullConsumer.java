package cn.note.RocketMQ.simple;


import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.store.ReadOffsetType;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.HashSet;
import java.util.Set;

/**
 * 拉模式
 */
public class PullConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("PullConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        Set<String> topics = new HashSet<>();
        topics.add("Simple");
        consumer.setRegisterTopics(topics);
        consumer.start();
        while (true) {
            consumer.getRegisterTopics().forEach(n -> {
                try {
                    Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues(n);
                    messageQueues.forEach(l -> {
                        try {
                            long offset = consumer.getOffsetStore().readOffset(l, ReadOffsetType.READ_FROM_MEMORY);
                            if (offset < 0) {
                                offset = consumer.getOffsetStore().readOffset(l, ReadOffsetType.READ_FROM_STORE);
                            }
                            if (offset < 0) {
                                offset = consumer.getOffsetStore().readOffset(l, ReadOffsetType.READ_FROM_STORE);
                            }
                            if (offset < 0) {
                                offset = 0;
                            }
                            PullResult pull = consumer.pull(l, "*", offset, 32);
                            switch (pull.getPullStatus()) {
                                case FOUND:
                                    pull.getMsgFoundList().forEach(k -> {
                                        System.out.println("消息循环拉去成功_" + k);
                                    });
                                    consumer.updateConsumeOffset(l, pull.getNextBeginOffset());
                            }
                        } catch (MQClientException e) {
                            throw new RuntimeException(e);
                        } catch (RemotingException e) {
                            throw new RuntimeException(e);
                        } catch (MQBrokerException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (MQClientException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
