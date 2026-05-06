package com.smartdine.ai.agent.core.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

/**
 * 用户服务Agent - 智能客服
 * 处理用户咨询、订单查询、投诉处理、售后支持
 */
@AiService
public interface CustomerServiceAgent {

    @SystemMessage(fromResource = "prompts/customer-service.txt")
    Flux<String> streamService(String userMessage);

    @SystemMessage(fromResource = "prompts/customer-service.txt")
    String service(String userMessage);
}
