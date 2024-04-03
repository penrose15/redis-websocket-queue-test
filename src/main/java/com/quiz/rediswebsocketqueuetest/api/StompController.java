package com.quiz.rediswebsocketqueuetest.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompController {

    @MessageMapping("/example") // 클라이언트가 /app/example로 메시지 전송
    @SendTo("/topic/messages")  // 서버가 /topic/messages로 메시지 전달
    public String example(String str) {
        return "success";
    }
}
