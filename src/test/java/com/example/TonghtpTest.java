package com.example;

import com.tongtech.client.exception.TLQBrokerException;
import com.tongtech.client.exception.TLQClientException;
import com.tongtech.client.message.Message;
import com.tongtech.client.producer.SendResult;
import com.tongtech.client.producer.SendStatus;
import com.tongtech.client.producer.TLQProducer;
import com.tongtech.client.remoting.exception.RemotingException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

/**
 * @author zengnianmei
 */
@SpringBootTest
public class TonghtpTest {
    @Resource
    private TLQProducer producer;

    @Test
    public void send() throws TLQBrokerException, RemotingException, TLQClientException, InterruptedException {
        Message message = new Message();
        message.setTopicOrQueue("umdc.topic.notice.inner");
        message.setBody("hello world".getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = producer.send(message);
        Assertions.assertEquals(sendResult.getSendStatus(), SendStatus.SEND_OK);
    }
}
