package com.quiz.rediswebsocketqueuetest.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void deleteRange(String key, Long start, Long end) {
        ZSetOperations<String, Object> zSetOperations = opsForZSet();
        zSetOperations.removeRange(key, start, end);
    }

    public ZSetOperations<String, Object> opsForZSet() {
        return redisTemplate.opsForZSet();
    }

    public void addQueue(String key, Object value, Long score) {
        try {
            opsForZSet().add(key, value, (double) score);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Set<Object> zRange(String key, Long start, Long end) {
        return opsForZSet().range(key, start, end);
    }

}
