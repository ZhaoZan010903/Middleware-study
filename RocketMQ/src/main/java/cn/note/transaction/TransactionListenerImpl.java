package cn.note.transaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 本地事务监听器
 */
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
