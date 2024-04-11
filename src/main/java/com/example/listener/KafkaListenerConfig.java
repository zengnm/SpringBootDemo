package com.example.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class KafkaListenerConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(KafkaListenerConfig.class);

    @KafkaListener(topics = "simple")
    public void processMessage(String content) {
        LOGGER.info("Received message: {}", content);
    }
}