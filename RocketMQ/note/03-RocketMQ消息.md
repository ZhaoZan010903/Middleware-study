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

* message.setDelayTimeLevel(3): 预定日常定时发送.1到18分别对应messageDelayLevel = 1s 5s 10s 30s 1m 2m ................1h
  2h;可以在dashboard中broker配置查看
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

### 4.批量消息

批量消息是指将多条消息合并成一个批量消息,一次发送出去.这样的好处是可以减少网络IO,提升吞吐量

批量消息的使用限制:

* 消息大小不能超过4M,虽然源码注释不能超过1M,但是实际使用不超过4M即可.平衡整体的性能,建议保持1M左右
* 相同Topic
* 相同的waitStoreMsgOK
* 不能是延迟消息、事务消息等

生产者代码:

```java
public class BatchProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("SyncProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Simple", "Tags", (i + "BatchProducer").getBytes(StandardCharsets.UTF_8));
            messages.add(message);
        }
        SendResult send = producer.send(messages);
        System.out.println("消息发送成功" + send);
        producer.shutdown();
    }
}
```

### 5.过滤消息

在大多数情况下，可以使用Message的Tag属性来简单快速的过滤消息；

#### 5.1 使用Tag方式来过滤

Tag是RocketMQ中特有的一个消息属性.

RocketMQ的最佳实践中就建议使用RocketMQ时,一个应用可以就用一个Topic,而应用中的不同业务就用Tag来区分

Tag方式有一个很大的限制,就是一个消息只能有一个Tag,这在一些比较复杂的场景就有点不足了.这时候可以使用SQL表达式来对消息进行过滤

```java
public class FilterTagProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("SyncProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        String[] tags = new String[]{"TagA", "TagB", "TagC"};
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Filter", tags[i % tags.length], (tags[i % tags.length] + "_SyncProducer").getBytes(StandardCharsets.UTF_8));
            SendResult send = producer.send(message);
            System.out.println(i + "消息发送成功" + send);
        }
        producer.shutdown();
    }
}
```

消费者:

```java
public class FilterTagConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("SimpleConsumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        // *表示不过滤 所有消息都消费
        consumer.subscribe("Filter", "TagA || TagC");
        //并发消费
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                list.forEach(n -> System.out.println("消息消费成功"+list));
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

#### 5.2 使用Sql方式

```java
public class FilterSqlProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("SyncProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        String[] tags = new String[]{"TagA", "TagB", "TagC"};
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Filter", tags[i % tags.length], (tags[i % tags.length] + "_SyncProducer").getBytes(StandardCharsets.UTF_8));
            message.putUserProperty("baili", String.valueOf(i));
            SendResult send = producer.send(message);
            System.out.println(i + "消息发送成功" + send);
        }
        producer.shutdown();
    }
}
```

### 6.事物消息

这个事务消息是RocketMQ提供的一个非常有特色的功能,需要着重理解.
[事务消息](https://rocketmq.apache.org/zh/docs/featureBehavior/04transactionmessage)

#### 6.1 什么是事物消息

事务消息是在分布式系统中保证最终的一致性的两阶段提交的消息实现.他可以保证本地事务执行与消息发送两个操作的原子性,也就是这两个操作一起成功或者一起失败.

* 事务消息是RocketMQ提供的一种高级消息类型，支持在分布式场景下保证消息生产和本地事务的最终一致性。
* 事务消息发送分为两个阶段：第一阶段发送半事务消息，即暂不能投递的消息；第二阶段根据本地事务的执行结果，向服务端提交二次确认结果，即提交或回滚消息。
* 服务端会对半事务消息进行定时扫描，如果发现某条消息长期处于未知状态，会向生产者发起回查请求，询问该消息的最终状态。
* 生产者需要实现TransactionListener接口，重写executeLocalTransaction和checkLocalTransaction两个方法，分别用于执行本地事务和回查本地事务。
* 事务消息有以下几个限制：
  * 消息类型必须和主题类型一致；
  * 消费端需要自行保证消费结果的正确性；
  * 中间状态可能存在不一致性；
  * 事务超时可能导致强制回滚。
#### 6.2 事务消息实现

生产者代码:

```java
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
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Transaction",
                    tags[i % tags.length],
                    (tags[i % tags.length] + "_TransactionProducer").getBytes(StandardCharsets.UTF_8));
            TransactionSendResult transactionSendResult = producer.sendMessageInTransaction(message, null);
            System.out.println("消息发送成功_" + transactionSendResult);
            Thread.sleep(10);
        }
        Thread.sleep(100000);
        producer.shutdown();
    }
}
```

##### 6.2.1 TransactionListener

*
executeLocalTransaction：该方法用于执行本地事务，即在发送半消息后，执行业务逻辑，并返回本地事务的状态。该方法有两个参数，分别是message和o。message是发送的半消息，包含了消息的主题、标签、键值等信息。o是发送消息时传入的参数，可以用于传递一些额外的信息。该方法的返回值是LocalTransactionState类型，表示本地事务的状态，可以是以下三种之一：
    * COMMIT_MESSAGE：表示本地事务执行成功，提交消息，让消费者可以消费该消息。
    * ROLLBACK_MESSAGE：表示本地事务执行失败，回滚消息，让消费者不会消费该消息。
    * UNKNOW：表示本地事务的状态未知，需要由Broker发起回查来确定状态。
*
checkLocalTransaction：该方法用于回查本地事务的状态，即在Broker发现某个半消息长时间处于未知状态时，主动向生产者询问该消息对应的本地事务的状态，并根据回查结果更新半消息的状态。该方法有一个参数，就是messageExt，它是Message的子类，包含了半消息的所有信息，以及一些额外的属性。该方法的返回值也是LocalTransactionState类型，同样表示本地事务的状态，可以是以下三种之一：
* COMMIT_MESSAGE：表示本地事务执行成功，提交消息，让消费者可以消费该消息。
* ROLLBACK_MESSAGE：表示本地事务执行失败，回滚消息，让消费者不会消费该消息。
* UNKNOW：表示本地事务的状态仍然未知，需要继续回查。

实例代码:

```java
public class TransactionListenerImpl implements TransactionListener {

    //执行本地事务
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        String tags = message.getTags();
        if (StringUtils.contains("TagA", tags)) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        if (StringUtils.contains("TagB", tags)) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else {
            return LocalTransactionState.UNKNOW;
        }
    }

    //回查本地事务
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        String tags = messageExt.getTags();
        if (StringUtils.contains("TagC", tags)) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        if (StringUtils.contains("TagD", tags)) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else {
            return LocalTransactionState.UNKNOW;
        }
    }
}
```

