# Rocket

1. 创建工程
2. 引入依赖

```xml

<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-client</artifactId>
    <version>5.1.0</version>
</dependency>
```

## RocketMQ生产者

### 1.基本样例

消费生产者分别通过三种方式发送消息:

* 同步发送:等待消息返回后再继续进行下面的操作.
    * 对要求可靠性高,流量峰值较小的场景;
* 异步发送:不等待消息返回直接进入后续流程.broker将结果返回后调用callback函数,并使用CountDownLatch计数
    * 流量峰值大,可靠性不高;
* 单向发送:只负责发送,不管消息是否发送成功
    * 发送效率高,安全性最低

#### 1.1 同步发送

使用场景:

1. 可靠性要求高
2. 数据量级较少
3. 实时响应

生产者代码:

```java
public class SyncProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("SyncProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Simple", "Tags", (i + "_SyncProducer").getBytes(StandardCharsets.UTF_8));
            SendResult send = producer.send(message);
            System.out.println(send);
        }
        producer.shutdown();
    }
}
```

消费者代码:

```java
public class Consumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("SimpleConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        // *表示不过滤 所有消息都消费
        consumer.subscribe("Simple", "*");
        //并发消费
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                list.forEach(n -> System.out.println("消息消费成功"));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("消费者启动成功");
    }
}
```

#### 1.2 异步发送

生产者代码:

```java
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
// 在发送消息的时候,send方法里面传入 SendCallback()
```

消费者代码:

```java
/**
 * 简单消费者
 */
public class Consumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("SimpleConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        // *表示不过滤 所有消息都消费
        consumer.subscribe("Simple", "*");
        //并发消费
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(i + "_消息消费成功_" + new String(list.get(i).getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("消费者启动成功");
    }
}
```

#### 1.3 单向发送

```java
/**
 * 单向发送
 * 应用场景:
 * 1.日志收集
 */
public class OnewayProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("AsyncProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message("Simple", "Tags", (i + "_SyncProducer").getBytes(StandardCharsets.UTF_8));
            //单向发送 没有返回值
            producer.sendOneway(message);
        }
        producer.shutdown();
    }
}
```

## RocketMQ消费者

### 2. 基本样例

* 拉模式:消费者主动去Broker上拉去消息
* 推模式:消费者等待Broker把消息推送过来

#### 2.1 拉模式

代码:

```java
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
```

通常情况下,用推模式比较简单。需要注意DefaultMQPullConsumer这个消费者类已标记为过期，但是还是可以使用的。替换的类是DefaultLitePullConsumerImpl

##### 2.1.1 拉模式 - 随机获取一个queue

```java
public class LitePull {
    public static void main(String[] args) throws MQClientException {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("LitePullConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.subscribe("Simple", "*");
        consumer.start();
        while (true) {
            List<MessageExt> messageExts = consumer.poll();
            System.out.println("消息拉去成功");
            messageExts.forEach(n -> {
                System.out.println("消息消费成功" + n);
            });
        }
    }
}
```

##### 2.1.2 拉模式 - 指定获取一个queue

```java
public class LitePullAssign {
    public static void main(String[] args) throws MQClientException {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("LitePullConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.start();
        Collection<MessageQueue> simple = consumer.fetchMessageQueues("Simple");
        ArrayList<MessageQueue> messageQueues = new ArrayList<>(simple);
        consumer.assign(messageQueues);
        consumer.seek(messageQueues.get(1), 10);
        while (true) {
            List<MessageExt> poll = consumer.poll();
            System.out.println("消息拉去成功");
            poll.forEach(n -> {
                System.out.println("消费成功_" + n);
            });
        }
    }
}
```