package com.smartdine.ai.agent.core.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * 调度指挥Agent (Dispatch Orchestrator Agent)
 * 订单履约的智能指挥官
 *
 * 核心能力：
 * - 智能派单：根据骑手位置、负载、天气自动分配订单
 * - 路径优化：实时规划最优配送路线
 * - 异常处理：自动处理超时、退单、餐损等异常
 * - 预测调度：基于历史数据预测订单高峰，提前调度骑手
 */
@AiService
public interface DispatchOrchestratorAgent {

    /**
     * 智能派单决策
     * @param orderId 订单ID
     * @return 派单决策结果
     */
    @SystemMessage(fromResource = "prompts/dispatch-orchestrator.txt")
    String dispatchOrder(Long orderId);

    /**
     * 配送异常处理
     * @param orderId 订单ID
     * @param exceptionType 异常类型
     * @return 处理方案
     */
    @SystemMessage(fromResource = "prompts/dispatch-orchestrator.txt")
    String handleException(Long orderId, String exceptionType);

}
