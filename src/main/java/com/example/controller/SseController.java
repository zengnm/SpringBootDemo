package com.example.controller;

import com.example.service.sse.SseEmitterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * SSE test
 *
 * @author zengnianmei
 */
@RestController
public class SseController {
    private final static Logger LOG = LoggerFactory.getLogger(SseController.class);
    private static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    @RequestMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam(name = "id") String id) {
        return SseEmitterManager.newAndConnect(id, -1L);
    }

    @RequestMapping("/push10")
    public String push(@RequestParam(name = "id") String id) {
        EXECUTOR.execute(() -> {
            for (int i = 1; i <= 10; i++) {
                SseEventBuilder event = SseEmitter.event().data(String.format("id=%s, index=%d, time=%s", id, i,
                        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
                try {
                    double v = new Random().nextDouble();
                    if (v < 0.1) {
                        throw new RuntimeException("异常啦~~~"+i);
                    }
                    SseEmitterManager.sendEarly(id, TimeUnit.SECONDS.toMillis(20), TimeUnit.SECONDS.toMillis(30),
                            event);
                } catch (Exception e) {
                    LOG.error("push_error, id={}, index={}, e=", id, i);
                    SseEmitterManager.completeWithError(id, e);
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            SseEmitterManager.complete(id);
        });

        return "start";
    }
}
