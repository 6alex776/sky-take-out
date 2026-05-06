package com.smartdine.ai.agent.core.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

/**
 * 智膳云核心AI Agent接口
 * 定义统一的AI服务契约，所有业务Agent基于此接口扩展
 */
@AiService
public interface SmartDineAgent {

    /**
     * 流式对话
     * @param userMessage 用户消息
     * @return 流式AI回复
     */
    @SystemMessage(fromResource = "prompts/core-agent.txt")
    Flux<String> streamChat(String userMessage);

    /**
     * 普通对话
     * @param userMessage 用户消息
     * @return AI回复
     */
    @SystemMessage(fromResource = "prompts/core-agent.txt")
    String chat(String userMessage);

}
