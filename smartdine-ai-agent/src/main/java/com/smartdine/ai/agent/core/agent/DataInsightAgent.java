package com.smartdine.ai.agent.core.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

/**
 * 数据洞察Agent - 智能数据分析
 * 经营报表解读、趋势预测、异常检测、决策建议
 */
@AiService
public interface DataInsightAgent {

    @SystemMessage(fromResource = "prompts/data-insight.txt")
    Flux<String> streamInsight(Long merchantId, String query);

    @SystemMessage(fromResource = "prompts/data-insight.txt")
    String insight(Long merchantId, String query);
}
