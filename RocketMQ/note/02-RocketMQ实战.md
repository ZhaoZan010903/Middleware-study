## Rocket实战

1. 创建工程
2. 引入依赖

```xml
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-client</artifactId>
    <version>5.1.0</version>
</dependency>
```

### 1.基本样例

消费生产者分别通过三种方式发送消息:

* 同步发送:等待消息返回后再继续进行下面的操作.
* 异步发送:不等待消息返回直接进入后续流程.broker将结果返回后调用callback函数,并使用CountDownLatch计数
* 单向发送:只负责发送,不管消息是否发送成功

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