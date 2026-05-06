package com.smartdine.ai.agent.core.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

/**
 * 供应链Agent - 智能供应链管理
 * 库存预警、采购建议、供应商评估、成本控制
 */
@AiService
public interface SupplyChainAgent {

    @SystemMessage(fromResource = "prompts/supply-chain.txt")
    Flux<String> streamAnalyze(Long merchantId, String query);

    @SystemMessage(fromResource = "prompts/supply-chain.txt")
    String analyze(Long merchantId, String query);
}
