## Rocket生产者

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
                for (int i=0;i< list.size();i++){
                    System.out.println(i+"_消息消费成功_"+new String(list.get(i).getBody()));
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

