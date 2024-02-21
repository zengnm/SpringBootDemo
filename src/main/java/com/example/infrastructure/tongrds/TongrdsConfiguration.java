package com.example.infrastructure.tongrds;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * tongrds配置
 *
 * @author zengnianmei
 */
@Configuration
public class TongrdsConfiguration {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // key序列化
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // value序列化
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }

    /**
     * fix
     * https://github.com/lettuce-io/lettuce-core/issues/1201
     * https://github.com/lettuce-io/lettuce-core/issues/1543
     */
    @Bean
    public LettuceClientConfigurationBuilderCustomizer redisBuilderCustomizer() {
        return builder -> builder.clientOptions(ClientOptions.builder().protocolVersion(ProtocolVersion.RESP2).build());
    }
}
