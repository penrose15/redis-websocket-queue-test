package com.quiz.rediswebsocketqueuetest.scheduler;

import com.quiz.rediswebsocketqueuetest.config.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class QueueScheduler {
    private final RedisUtil redisUtil;
    private final SimpMessagingTemplate messagingTemplate;

    private static String key = "KEY";

    @Scheduled(fixedDelay = 2000)
    public void queue() {
        //input
        int rand = (int) (Math.random() * 100);
        log.info("input = {}", rand);
        redisUtil.addQueue(key, rand, System.currentTimeMillis());

        //output
        Set<Object> sets = redisUtil.zRange(key, 0L,1L);
        for (Object set : sets) {
            int num = (int) set;
            log.info("output = {}", num);
            messagingTemplate.convertAndSend("/topic/queue",num);
        }
        redisUtil.deleteRange(key, 0L, 1L);
    }
}
