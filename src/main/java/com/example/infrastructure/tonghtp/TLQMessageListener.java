package com.example.infrastructure.tonghtp;

import com.tongtech.client.common.ModeType;
import com.tongtech.client.consumer.common.PullType;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * tlq消息监听
 *
 * @author zengnianmei
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface TLQMessageListener {

    String consumerGroup() default "DEFAULT_PUSH_CONSUMER";

    String topicOrQueue();

    String domain();

    int pullBatchSize() default 32;

    long nextOffset() default -1L;

    PullType pullType() default PullType.PullContinue;

    ModeType modeType() default ModeType.TOPIC;
}
