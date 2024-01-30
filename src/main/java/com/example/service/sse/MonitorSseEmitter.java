package com.example.service.sse;

import jakarta.annotation.Nonnull;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * 可监控的SseEmitter
 *
 * @author zengnianmei
 */
public class MonitorSseEmitter extends SseEmitter {
    /**
     * 开始时间，即SseEmitter创建时间
     */
    private final long startTime;
    /**
     * 客户端连接时间
     */
    private Long connectedTime;
    /**
     * 等待客户端连接超时时间，连接时间为: connectedTime - startTime
     */
    private final Long connectTimeout;

    /**
     * 最后发送消息时间
     */
    private Long lastSendTime;

    /**
     * 发送消息次数
     */
    private long sendTimes;

    public MonitorSseEmitter(Long connectTimeout, Long maxLifeTimeout) {
        super(maxLifeTimeout);
        this.startTime = System.currentTimeMillis();
        this.connectTimeout = connectTimeout;
    }

    public long getStartTime() {
        return startTime;
    }

    public Long getConnectedTime() {
        return connectedTime;
    }

    /**
     * @param connectedTime set时，创建时间NotNull
     */
    public void setConnectedTime(long connectedTime) {
        this.connectedTime = connectedTime;
    }

    public Long getConnectTimeout() {
        return connectTimeout;
    }

    public Long getSendTimes() {
        return sendTimes;
    }

    public Long getLastSendTime() {
        return lastSendTime;
    }

    @Override
    public void send(@Nonnull SseEventBuilder builder) throws IOException {
        super.send(builder);
        lastSendTime = System.currentTimeMillis();
        sendTimes += 1;
    }

    @Override
    public void send(@Nonnull Object object) throws IOException {
        super.send(object);
        lastSendTime = System.currentTimeMillis();
        sendTimes += 1;
    }

    @Override
    public void send(@Nonnull Object object, MediaType mediaType) throws IOException {
        super.send(object, mediaType);
        lastSendTime = System.currentTimeMillis();
        sendTimes += 1;
    }
}