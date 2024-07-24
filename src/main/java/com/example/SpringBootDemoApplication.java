package com.example;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.core.PulsarTemplate;

@SpringBootApplication
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(PulsarTemplate<String> pulsarTemplate) {
        return (args) -> pulsarTemplate.send("persistent://notice/test/test_simple_topic", "Hello Pulsar World!");
    }

    @PulsarListener(subscriptionName = "hello-pulsar-sub", topics = "persistent://notice/test/test_simple_topic")
    void listen(String message) {
        System.out.println("Message Received: " + message);
    }
}