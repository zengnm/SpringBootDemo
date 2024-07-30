package com.example;

import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.core.PulsarTemplate;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SpringBootDemoApplication {
    private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(PulsarTemplate<String> pulsarTemplate) {
        return (args) -> executor.scheduleAtFixedRate(() -> {
            try {
                pulsarTemplate.send("persistent://notice/test/test_simple_topic",
                        "Hello Pulsar World!"+new Random().nextInt());
            } catch (PulsarClientException e) {
                throw new RuntimeException(e);
            }
        }, 3, 90, TimeUnit.SECONDS);
    }

    @PulsarListener(subscriptionName = "hello-pulsar-sub", topics = "persistent://notice/test/test_simple_topic")
    void listen(String message) {
        System.out.println("Message Received: "+message);
    }
}