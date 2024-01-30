package com.example.service.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SseEmitter管理器
 * 1、全部API基于id，不暴露SseEmitter（直接暴露可能已经关闭重连）, 目的：支持自动重连 + 提前发送功能
 * 2、缓存管理：需要考虑缓存不释放导致内存溢出问题，因此需要自动删除，包括：客户端断开连接、异常、完成、等待连接超时、连接超时
 *
 * @author zengnianmei
 */
public class SseEmitterManager {
    // todo 考虑使用lru缓存
    private final static Map<String, MonitorSseEmitter> SSE_EMITTER_CACHE = new ConcurrentHashMap<>();

    private final static ScheduledExecutorService connectTimeoutSchedule;

    static {
        ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
        AtomicLong count = new AtomicLong(0);
        ThreadFactory threadFactory = r -> {
            Thread thread = defaultThreadFactory.newThread(r);
            thread.setName(String.format("SSE-CONNECT-TIMEOUT-HANDLE-%d", count.getAndIncrement()));
            return thread;
        };
        connectTimeoutSchedule = Executors.newScheduledThreadPool(1, threadFactory);
        connectTimeoutSchedule.scheduleAtFixedRate(new ConnectTimeoutTask(), 100, 100, TimeUnit.MILLISECONDS);
    }

    private static MonitorSseEmitter newIfAbsent(String id, Long connectTimeout, Long maxLifeTimeout, boolean connect) {
        return SSE_EMITTER_CACHE.computeIfAbsent(id, key -> {
            MonitorSseEmitter sseEmitter = new MonitorSseEmitter(connectTimeout, maxLifeTimeout);
            if (connect) {
                sseEmitter.setConnectedTime(System.currentTimeMillis());
            }
            // 当且仅当缓存中的sseEmitter为当前待删除sseEmitter才执行删除
            Runnable delete = () -> SSE_EMITTER_CACHE.compute(id, (key1, old) -> old == sseEmitter ? null : old);
            // 已完成、错误、超时场景时从缓存中删除
            sseEmitter.onCompletion(delete);
            sseEmitter.onTimeout(delete);
            sseEmitter.onError((t) -> delete.run());
            return sseEmitter;
        });
    }

    /**
     * 注意：除了作为mvc最终返回外，其他地方不要直接获取SseEmitter
     */
    public static MonitorSseEmitter newAndConnect(String id, Long maxLifeTimeout) {
        // 关闭旧连接
        MonitorSseEmitter remotedEmitter = SSE_EMITTER_CACHE.get(id);
        if (remotedEmitter != null && remotedEmitter.getConnectedTime() != null) {
            SSE_EMITTER_CACHE.remove(id);
            remotedEmitter.complete();
        }
        return newIfAbsent(id, null, maxLifeTimeout, true);
    }

    public static boolean exist(String id) {
        return SSE_EMITTER_CACHE.containsKey(id);
    }

    /**
     * 发送消息，如果SseEmitter不存在会忽略并返回false (不保证消息触达率)
     */
    public static boolean send(String id, SseEventBuilder eventBuilder) throws IOException {
        MonitorSseEmitter sseEmitter = SSE_EMITTER_CACHE.get(id);
        if (sseEmitter == null) {
            return false;
        }
        sseEmitter.send(eventBuilder);
        return true;
    }

    /**
     * 提前发送消息，如果SseEmitter不存在会新建一个，客户端未消费会把消息暂存
     * 为防止长期未连接导致资源无法释放以及消息积压，需要设置支持connectTimeout功能
     */
    public static boolean sendEarly(String id, Long connectTimeout, Long maxLifeTimeout,
                                    SseEventBuilder eventBuilder) throws IOException {
        newIfAbsent(id, connectTimeout, maxLifeTimeout, false).send(eventBuilder);
        return true;
    }

    public static boolean complete(String id) {
        MonitorSseEmitter sseEmitter = SSE_EMITTER_CACHE.get(id);
        if (sseEmitter == null) {
            return false;
        }
        sseEmitter.complete();
        return true;
    }

    public static boolean completeWithError(String id, Throwable throwable) {
        MonitorSseEmitter sseEmitter = SSE_EMITTER_CACHE.get(id);
        if (sseEmitter == null) {
            return false;
        }
        sseEmitter.completeWithError(throwable);
        return true;
    }

    private static class ConnectTimeoutTask implements Runnable {
        @Override
        public void run() {
            long current = System.currentTimeMillis();
            Iterator<MonitorSseEmitter> iterator = SseEmitterManager.SSE_EMITTER_CACHE.values().iterator();
            while (iterator.hasNext()) {
                MonitorSseEmitter emitter = iterator.next();
                if (emitter.getConnectTimeout() == null) {
                    continue;
                }
                // 未连接且已超时的情况，需要清除缓存
                Long connectedTime = emitter.getConnectedTime();
                if (connectedTime == null && current-emitter.getStartTime() > emitter.getConnectTimeout()) {
                    iterator.remove();
                    emitter.complete();
                }
            }
        }
    }
}
