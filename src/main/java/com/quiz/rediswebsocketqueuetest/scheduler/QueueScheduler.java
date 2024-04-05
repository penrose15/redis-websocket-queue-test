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

    /*
    * ## flow
    * 2초마다 redis sortedSet에 숫자 추가
    * sorted set는 System.currentTimeMillis() 순으로 정렬
    *
    * sorted set에서 먼저 들어온 순으로 숫자 꺼냄
    * 이를 /topic/queue를 구독한 클라이언트에게 전송
    * 이후 꺼낸 숫자 삭제
    * */

    @Scheduled(fixedDelay = 2000)
    public void queue() {
        //input
        int rand = (int) (Math.random() * 100);
        log.info("input = {}", rand);
        redisUtil.addQueue(key, rand, System.currentTimeMillis());

        Long size = redisUtil.opsForZSet().size(key);

        if(size == null || size < 10L) return;
        //output
        Set<Object> sets = redisUtil.zRange(key, 0L,10L);
        for (Object set : sets) {
            int num = (int) set;
            log.info("output = {}", num);
            messagingTemplate.convertAndSend("/topic/queue",num);
        }
        redisUtil.deleteRange(key, 0L, 1L);
    }
}
