package com.example.controller;

import com.example.persistence.TestDataRepository;
import com.example.persistence.entity.TestData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequestMapping("/kafka")
@RestController
public class KafkaController {
    private final TestDataRepository testDataRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaController(TestDataRepository testDataRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.testDataRepository = testDataRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send")
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public String sendMessage(@RequestBody String message) throws InterruptedException {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
//                   消息将在数据库事务提交之后发送
                kafkaTemplate.send("simple", message);
//                throw new RuntimeException("1111"); 这里抛出异常，将传播至调用方(http异常)，但数据库事务继续提交、消息正常发送
            }
        });
//        kafkaTemplate.send("simple", message+1);

        TimeUnit.SECONDS.sleep(1);
        testDataRepository.save(new TestData().setName("111").setStatus(1));
        if ("rollback".equals(message)) {
            throw new RuntimeException("触发事务回滚"); // 消息发布在事务中，抛出异常将回滚：消息不发送、数据库不更新
        }
        return "success";
    }
}