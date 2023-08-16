## RocketMQ消息

### 1.顺序消息

顺序消息指生产者局部有序发送到一个queue，但多个queue之间是全局无序的。

* 顺序消息生产者样例：通过MessageQueueSelector将消息有序发送到同一个queue中
* 顺序消息消费者样例：通过MessageListenerOrderly消费者每次读取消息都只从一个queue中获取(通过加锁的方式实现)

生产者

```java
public class OrderProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("OrderProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                Message message = new Message("Order", "TagA", ("order_" + i + "_step_" + j).getBytes(StandardCharsets.UTF_8));
                SendResult send = producer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        Integer id = (Integer) o;
                        int index = id % list.size();
                        return list.get(index);
                    }
                }, i);
                System.out.println("发送消息成功_" + send);
            }
        }
        producer.shutdown();
    }
}
```

消费者

```java
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

```

### 2.广播消息

广播消息并没有特定的消息消费者样例,这是因为这涉及到消费者的集群消费模式.

* MessageModel.BROADCASTING: 广播消息.一条消息会发送给所有订阅了对应主题的消费者,不管消费者是不是同一个消费者组.
* MessageModel.CLUSTERING: 集群消息.每一条消息只会被同一个消费者组中的一个实例消费

```java
public class BroadcastConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("SimpleConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        // *表示不过滤 所有消息都消费
        consumer.subscribe("Simple", "*");
        // 设置广播模式    setMessageModel设置模式
        //CLUSTERING 集群模式   BROADCASTING 广播模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
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


### 3.延迟消息 
延迟消息实现的效果就是在调用producer.send方法后,消息并不会立即发生出去,而是会等一段时间再发生出去.这是RocketMQ特有的一个功能.
* message.setDelayTimeLevel(3): 预定日常定时发送.1到18分别对应messageDelayLevel = 1s 5s 10s 30s 1m 2m ................1h 2h;可以在dashboard中broker配置查看
* msg.setDelayTimeMs(10L): 指定时间定时发送.默认支持最大延迟时间为3天,可以根据broker配置: timerMaxDelaySec修改


生产者
```java
public class ScheduleProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("ScheduleProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Simple", "Tags", (i + "ScheduleProducer").getBytes(StandardCharsets.UTF_8));

            // 延时设置 等级
//            message.setDelayTimeLevel(3);
            
            //延时设置 10秒之后发送 最多3天
            message.setDelayTimeMs(10000L);

            SendResult send = producer.send(message);
            System.out.println(i + "消息发送成功" + send + LocalDateTime.now());
        }
        producer.shutdown();
    }
}
```

消费者
```java
public class ScheduleConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ScheduleConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        // *表示不过滤 所有消息都消费
        consumer.subscribe("Simple", "*");
        //并发消费
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(i + "_消息消费成功_" + new String(list.get(i).getBody()) + LocalDateTime.now());
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("消费者启动成功");
    }
}
```