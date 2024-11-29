package com.example.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.support.Acknowledgment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengnianmei
 */
public class KafkaListenerBean extends AbstractKafkaListener {
    private int attempts = 3;

    public KafkaListenerBean(ConcurrentKafkaListenerContainerFactory<?, ?> containerFactory, String topics, String topicPattern, String groupId, int concurrency, List<String> properties) {
        super(containerFactory, topics, topicPattern, groupId, concurrency, properties);
    }

    /**
     * 批量消费和RetryableTopic组合: Non-blocking retries are not supported with Batch Listeners. Since 3.2
     */
//    @RetryableTopic(attempts = "#{__listener.attempts}")
    @KafkaListener(containerFactory = "#{__listener.containerFactory}",
            topics = "#{__listener.topics}",
            groupId = "#{__listener.groupId}",
            batch = "true", // 注意batch不支持__listener
            concurrency = "#{__listener.concurrency}",
            properties = "#{__listener.properties}")
    public void onMessage(List<ConsumerRecord<byte[], byte[]>> records, Acknowledgment acknowledgment) {
        List<String> values = new ArrayList<>();
        records.iterator().forEachRemaining(record -> values.add(new String(record.value())));
        System.out.printf("contents: %s%n", String.join(",", values));
        acknowledgment.acknowledge();
    }
//    public void onMessage(List<byte[]> records, Acknowledgment acknowledgment) {
////        String contents = records.stream().map(r -> new String(r.value())).collect(Collectors.joining(","));
//        String contents = records.stream().map(String::new).collect(Collectors.joining(","));
////        String contents = records.stream().map(e -> new String(e.value().get())).collect(Collectors.joining(","));
//        System.out.printf("groupId: %s, contents: %s%n", groupId,  contents);
//        acknowledgment.acknowledge();
//    }
}
