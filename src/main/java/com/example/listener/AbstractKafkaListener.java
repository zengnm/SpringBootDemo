package com.example.listener;

import lombok.Getter;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import java.util.List;

/**
 * @author zengnianmei
 */
@Getter
public class AbstractKafkaListener {
    private final ConcurrentKafkaListenerContainerFactory<?, ?> containerFactory;
    private final String topics;
    private final String topicPattern;
    private final String groupId;
    private final int concurrency;
    private final List<String> properties;

    public AbstractKafkaListener(ConcurrentKafkaListenerContainerFactory<?, ?> containerFactory, String topics, String topicPattern, String groupId, int concurrency, List<String> properties) {
        this.containerFactory = containerFactory;
        this.topics = topics;
        this.topicPattern = topicPattern;
        this.groupId = groupId;
        this.concurrency = concurrency;
        this.properties = properties;
    }

}
