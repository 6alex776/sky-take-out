package com.smartdine.ai.agent.client;

import com.smartdine.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

/**
 * 订单服务Feign客户端
 */
@FeignClient(name = "smartdine-service", contextId = "orderServiceClient")
public interface OrderServiceClient {

    @GetMapping("/admin/order/statistics")
    Result<Map<String, Object>> getOrderStatistics(
            @RequestParam("begin") LocalDate begin,
            @RequestParam("end") LocalDate end);

    @GetMapping("/admin/order/detail/{id}")
    Result<Map<String, Object>> getOrderDetail(@PathVariable("id") Long id);
}
