package com.example.infrastructure.tonghtp;

import com.tongtech.client.common.ModeType;
import com.tongtech.client.consumer.impl.TLQPushConsumer;
import com.tongtech.client.consumer.listener.MessageListener;
import com.tongtech.client.exception.TLQClientException;
import com.tongtech.client.producer.TLQProducer;
import lombok.SneakyThrows;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * tonghtp配置
 *
 * @author zengnianmei
 */
@Configuration
public class TonghtpConfiguration implements ApplicationContextAware, SmartInitializingSingleton {
    @Value("${tlq.name-srv-url}")
    private String namesrvAddr;
    @Value("${tlq.domain}")
    private String domain;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public TLQProducer producer() throws TLQClientException {
        TLQProducer producer = new TLQProducer();
        producer.setNamesrvAddr(namesrvAddr);
        producer.setDomain(domain);
        producer.setModeType(ModeType.TOPIC);
        return producer;
    }

    @SneakyThrows
    @Override
    public void afterSingletonsInstantiated() {
        List<Object> beans = this.applicationContext.getBeansWithAnnotation(TLQMessageListener.class).entrySet().stream()
                .filter((entry) -> !ScopedProxyUtils.isScopedTarget(entry.getKey()))
                .map(Map.Entry::getValue)
                .toList();
        for (Object bean : beans) {
            Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);
            TLQPushConsumer consumer = getTlqPushConsumer(clazz);
            consumer.registerMessageListener((MessageListener)bean);
            consumer.start();
        }
    }

    private TLQPushConsumer getTlqPushConsumer(Class<?> clazz) throws TLQClientException {
        TLQMessageListener annotation = clazz.getAnnotation(TLQMessageListener.class);
        TLQPushConsumer consumer = new TLQPushConsumer();
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumerGroup(annotation.consumerGroup());
        consumer.subscribe(annotation.topicOrQueue());
        consumer.setDomain(annotation.domain());
        consumer.setModeType(annotation.modeType());
        consumer.setPullBatchSize(annotation.pullBatchSize());
        consumer.setNextOffset(annotation.nextOffset());
        consumer.setPullType(annotation.pullType());
        return consumer;
    }
}
