package com.smartdine.ai.agent.controller;

import com.smartdine.ai.agent.core.service.AgentOrchestrator;
import com.smartdine.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * AI Agent聊天控制器
 * 提供智能路由的Agent聊天接口，支持流式输出
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AgentChatController {

    private final AgentOrchestrator agentOrchestrator;

    /**
     * 智能Agent聊天 - 自动路由到对应Agent（流式SSE输出）
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody ChatRequest request) {
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        Long merchantId = request.getMerchantId() != null ? request.getMerchantId() : 1L;
        log.info("AI智能聊天请求, sessionId: {}, merchantId: {}, message: {}", sessionId, merchantId, request.getMessage());
        return agentOrchestrator.route(request.getMessage(), merchantId, sessionId);
    }

    /**
     * 同步聊天接口（非流式，自动路由）
     */
    @PostMapping("/chat/sync")
    public Result<String> chatSync(@RequestBody ChatRequest request) {
        Long merchantId = request.getMerchantId() != null ? request.getMerchantId() : 1L;
        String response = agentOrchestrator.routeSync(request.getMessage(), merchantId);
        return Result.success(response);
    }

    // 内部请求类
    @lombok.Data
    public static class ChatRequest {
        private String message;
        private String sessionId;
        private Long merchantId;
    }
}
