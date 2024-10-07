package com.example.controller;

import com.example.sample.HelloRequest;
import com.example.sample.ReqBody;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author zengnianmei
 */
@RequestMapping("/mq")
@RestController
public class MqController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MqController.class);
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String sendProtobuf() {
        String exchange = "exchange.vip.protobuf.test";
        String routingKey = "rk.vip.protobuf.test";
        long millis = System.currentTimeMillis();
        HelloRequest build = HelloRequest.newBuilder()
                .setCode((int) (millis % 100))
                .setMessage("message: "+millis)
                .setData(ReqBody.newBuilder()
                        .setNum((int) (millis % 47))
                        .setName(UUID.randomUUID().toString())
                ).build();
        Message message = new Message(build.toByteArray());
//        rabbitTemplate.send(exchange, routingKey, message);
        rabbitTemplate.invoke(op -> {
            rabbitTemplate.send(exchange, routingKey, message);
            boolean flag = rabbitTemplate.waitForConfirms(2000);
            LOGGER.info("flag: {}", flag);
            return flag;
        }, (deliveryTag, multiple) -> {
            LOGGER.info("【SIMPLE waitForConfirms 形式】ack - deliveryTag：${}$, multiple：${}$", deliveryTag, multiple);
        }, (deliveryTag, multiple) -> {
            LOGGER.info("【SIMPLE waitForConfirms 形式】nack - deliveryTag：${}$, multiple：${}$", deliveryTag, multiple);
        });
        return "success";
    }
}
