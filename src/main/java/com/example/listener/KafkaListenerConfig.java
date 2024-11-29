package com.example.listener;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import java.util.List;

@Configuration
public class KafkaListenerConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(KafkaListenerConfig.class);

//    @KafkaListener(topics = "simple")
//    public void processMessage(String content, Acknowledgment ack) {
//        LOGGER.info("Received message: {}", content);
//    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KafkaListenerBean kafkaListenerBean(ConcurrentKafkaListenerContainerFactory<byte[], byte[]> containerFactory,
                                               String topic, String groupId, int concurrency) {
        return new KafkaListenerBean(containerFactory, topic, "", groupId, concurrency,
                List.of(ConsumerConfig.MAX_POLL_RECORDS_CONFIG+"=3"));
    }
}