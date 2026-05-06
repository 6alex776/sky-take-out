package com.smartdine.ai.agent.controller;

import com.smartdine.ai.agent.core.agent.*;
import com.smartdine.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * AI Agent聊天控制器
 * 提供各Agent的聊天接口，支持流式输出
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AgentChatController {

    private final SmartDineAgent smartDineAgent;
    private final BusinessAdvisorAgent businessAdvisorAgent;
    private final CustomerServiceAgent customerServiceAgent;
    private final DataInsightAgent dataInsightAgent;

    /**
     * 核心Agent聊天 - 通用入口
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody ChatRequest request) {
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        log.info("AI聊天请求, sessionId: {}, message: {}", sessionId, request.getMessage());
        return smartDineAgent.streamChat(request.getMessage())
                .map(chunk -> "data: " + chunk + "\n\n")
                .concatWith(Flux.just("data: [DONE]\n\n"));
    }

    /**
     * 经营顾问Agent
     */
    @PostMapping(value = "/business-advisor", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> businessAdvisor(
            @RequestParam Long merchantId,
            @RequestBody ChatRequest request) {
        return businessAdvisorAgent.diagnose(merchantId, request.getMessage())
                .map(chunk -> "data: " + chunk + "\n\n")
                .concatWith(Flux.just("data: [DONE]\n\n"));
    }

    /**
     * 用户服务Agent
     */
    @PostMapping(value = "/customer-service", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> customerService(@RequestBody ChatRequest request) {
        return customerServiceAgent.streamService(request.getMessage())
                .map(chunk -> "data: " + chunk + "\n\n")
                .concatWith(Flux.just("data: [DONE]\n\n"));
    }

    /**
     * 数据洞察Agent
     */
    @PostMapping(value = "/data-insight", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> dataInsight(
            @RequestParam Long merchantId,
            @RequestBody ChatRequest request) {
        return dataInsightAgent.streamInsight(merchantId, request.getMessage())
                .map(chunk -> "data: " + chunk + "\n\n")
                .concatWith(Flux.just("data: [DONE]\n\n"));
    }

    /**
     * 同步聊天接口（非流式）
     */
    @PostMapping("/chat/sync")
    public Result<String> chatSync(@RequestBody ChatRequest request) {
        String response = smartDineAgent.chat(request.getMessage());
        return Result.success(response);
    }

    // 内部请求类
    @lombok.Data
    public static class ChatRequest {
        private String message;
        private String sessionId;
    }
}
