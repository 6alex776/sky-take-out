package com.smartdine.ai.agent.client;

import com.smartdine.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * 工作台服务Feign客户端
 * 获取今日营业数据、订单概览等
 */
@FeignClient(name = "smartdine-service", contextId = "workspaceServiceClient")
public interface WorkspaceServiceClient {

    @GetMapping("/admin/workspace/businessData")
    Result<Map<String, Object>> getBusinessData();

    @GetMapping("/admin/workspace/overviewDishes")
    Result<Map<String, Object>> getOverviewDishes();

    @GetMapping("/admin/workspace/overviewSetmeals")
    Result<Map<String, Object>> getOverviewSetmeals();

    @GetMapping("/admin/workspace/overviewOrders")
    Result<Map<String, Object>> getOverviewOrders();
}
