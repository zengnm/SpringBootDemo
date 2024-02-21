package com.example.listener;

import com.example.infrastructure.tonghtp.TLQMessageListener;
import com.tongtech.client.consumer.PullResult;
import com.tongtech.client.consumer.listener.ConsumeConcurrentlyContext;
import com.tongtech.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.tongtech.client.consumer.listener.MessageListenerConcurrently;
import com.tongtech.client.exception.TLQClientException;

/**
 * 主题：umdc.topic.notice.inner，消息监听
 *
 * @author zengnianmei
 */
@TLQMessageListener(domain = "umdc", topicOrQueue = "umdc.topic.notice.inner")
public class MyMessageListener implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(PullResult pullResult, ConsumeConcurrentlyContext consumeConcurrentlyContext) throws TLQClientException, InterruptedException {
        System.out.println("========接收到mq======");
        System.out.println("topic:"+pullResult.getTopic());
        pullResult.getMsgFoundList().forEach(e -> System.out.println(new String(e.getBody())));

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}