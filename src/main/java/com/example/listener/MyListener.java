package com.example.listener;

import com.example.sample.HelloRequest;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zengnianmei
 */
@Component
public class MyListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyListener.class);

    @RabbitListener(queues = "queue.vip.protobuf.test")
    public void listen(byte[] msgBody) throws InvalidProtocolBufferException {
        HelloRequest helloRequest = HelloRequest.parseFrom(msgBody);
        LOGGER.info("Protobuf Message Received:{}", helloRequest);
    }
}
