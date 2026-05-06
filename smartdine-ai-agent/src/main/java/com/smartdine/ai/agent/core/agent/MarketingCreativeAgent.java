package com.smartdine.ai.agent.core.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * 营销创意Agent (Marketing Creative Agent)
 * 自动化的营销团队
 *
 * 核心能力：
 * - 活动生成：根据节日/热点自动生成营销活动
 * - 文案创作：自动生成菜品描述、推广文案、海报文案
 * - 效果预测：预测活动ROI，避免亏本营销
 * - A/B测试：自动对比不同文案效果
 */
@AiService
public interface MarketingCreativeAgent {

    /**
     * 生成营销活动方案
     * @param occasion 节日/热点
     * @param budget 预算
     * @return 活动方案
     */
    @SystemMessage(fromResource = "prompts/marketing-creative.txt")
    String createCampaign(String occasion, Double budget);

    /**
     * 生成营销文案
     * @param productName 产品名称
     * @param style 文案风格
     * @return 营销文案
     */
    @SystemMessage(fromResource = "prompts/marketing-creative.txt")
    String generateCopy(String productName, String style);

}
