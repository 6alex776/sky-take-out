package com.smartdine.ai.agent.core.service;

import com.smartdine.ai.agent.core.agent.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.regex.Pattern;

/**
 * Agent编排服务
 * 根据用户输入识别意图，路由到对应的Agent处理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgentOrchestrator {

    private final SmartDineAgent smartDineAgent;
    private final BusinessAdvisorAgent businessAdvisorAgent;
    private final CustomerServiceAgent customerServiceAgent;
    private final MarketingCreativeAgent marketingCreativeAgent;
    private final DataInsightAgent dataInsightAgent;

    // 意图匹配模式
    private static final Pattern BUSINESS_PATTERN = Pattern.compile(
            "(营业额|收入|利润|成本|亏损|经营|业绩|销售|营收|客单价|翻台率|菜品分析|经营建议|怎么提升|如何优化|生意不好)"
    );
    private static final Pattern MARKETING_PATTERN = Pattern.compile(
            "(营销|活动|推广|文案|海报|朋友圈|广告|促销|折扣|满减|会员|拉新|裂变|创意)"
    );
    private static final Pattern DATA_PATTERN = Pattern.compile(
            "(数据|报表|统计|分析|趋势|预测|对比|排名|top10|TOP10|销量|图表)"
    );
    private static final Pattern SERVICE_PATTERN = Pattern.compile(
            "(订单查询|退款|投诉|售后|客服|帮助|问题|怎么办|怎么解决|催单|取消订单)"
    );

    /**
     * 智能路由 - 根据用户消息选择对应的Agent
     */
    public Flux<String> route(String userMessage, Long merchantId, String sessionId) {
        log.info("Agent路由, sessionId: {}, message: {}", sessionId, userMessage);

        String lowerMsg = userMessage.toLowerCase();

        // 经营顾问Agent
        if (BUSINESS_PATTERN.matcher(userMessage).find()) {
            log.info("路由到经营顾问Agent");
            return businessAdvisorAgent.diagnose(merchantId, userMessage)
                    .map(chunk -> "data: " + chunk + "\n\n")
                    .concatWith(Flux.just("data: [DONE]\n\n"));
        }

        // 营销创意Agent
        if (MARKETING_PATTERN.matcher(userMessage).find()) {
            log.info("路由到营销创意Agent");
            return Flux.fromArray(marketingCreativeAgent.createCampaign(userMessage, null).split(""))
                    .map(chunk -> "data: " + chunk + "\n\n")
                    .concatWith(Flux.just("data: [DONE]\n\n"));
        }

        // 数据洞察Agent
        if (DATA_PATTERN.matcher(userMessage).find()) {
            log.info("路由到数据洞察Agent");
            return dataInsightAgent.streamInsight(merchantId, userMessage)
                    .map(chunk -> "data: " + chunk + "\n\n")
                    .concatWith(Flux.just("data: [DONE]\n\n"));
        }

        // 用户服务Agent
        if (SERVICE_PATTERN.matcher(userMessage).find()) {
            log.info("路由到用户服务Agent");
            return customerServiceAgent.streamService(userMessage)
                    .map(chunk -> "data: " + chunk + "\n\n")
                    .concatWith(Flux.just("data: [DONE]\n\n"));
        }

        // 默认使用核心Agent
        log.info("路由到核心Agent");
        return smartDineAgent.streamChat(userMessage)
                .map(chunk -> "data: " + chunk + "\n\n")
                .concatWith(Flux.just("data: [DONE]\n\n"));
    }

    /**
     * 同步路由
     */
    public String routeSync(String userMessage, Long merchantId) {
        String lowerMsg = userMessage.toLowerCase();

        if (BUSINESS_PATTERN.matcher(userMessage).find()) {
            return businessAdvisorAgent.advise(merchantId, userMessage);
        }
        if (MARKETING_PATTERN.matcher(userMessage).find()) {
            return marketingCreativeAgent.createCampaign(userMessage, null);
        }
        if (DATA_PATTERN.matcher(userMessage).find()) {
            return dataInsightAgent.insight(merchantId, userMessage);
        }
        if (SERVICE_PATTERN.matcher(userMessage).find()) {
            return customerServiceAgent.service(userMessage);
        }

        return smartDineAgent.chat(userMessage);
    }
}
