package com.smartdine.ai.agent.core.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

/**
 * 经营顾问Agent (Business Advisor Agent)
 * 每个商家的专属AI经营合伙人
 *
 * 核心能力：
 * - 智能诊断：自动分析店铺数据，发现经营问题
 * - 动态定价：根据时段、天气、库存自动建议菜品价格
 * - 菜品优化：分析差评和销量，建议菜品改良/下架
 * - 竞品监控：对比同区域竞品，给出差异化建议
 */
@AiService
public interface BusinessAdvisorAgent {

    /**
     * 经营诊断分析
     * @param merchantId 商家ID
     * @param query 用户查询
     * @return 流式诊断报告
     */
    @SystemMessage(fromResource = "prompts/business-advisor.txt")
    Flux<String> diagnose(Long merchantId, String query);

    /**
     * 获取经营建议
     * @param merchantId 商家ID
     * @param query 用户问题
     * @return AI建议
     */
    @SystemMessage(fromResource = "prompts/business-advisor.txt")
    String advise(Long merchantId, String query);

}
