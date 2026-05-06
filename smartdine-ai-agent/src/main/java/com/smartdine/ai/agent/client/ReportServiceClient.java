package com.smartdine.ai.agent.client;

import com.smartdine.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

/**
 * 报表服务Feign客户端
 */
@FeignClient(name = "smartdine-service", contextId = "reportServiceClient")
public interface ReportServiceClient {

    @GetMapping("/admin/report/turnover")
    Result<Map<String, Object>> getTurnoverStatistics(
            @RequestParam("begin") LocalDate begin,
            @RequestParam("end") LocalDate end);

    @GetMapping("/admin/report/user")
    Result<Map<String, Object>> getUserStatistics(
            @RequestParam("begin") LocalDate begin,
            @RequestParam("end") LocalDate end);

    @GetMapping("/admin/report/order")
    Result<Map<String, Object>> getOrderReport(
            @RequestParam("begin") LocalDate begin,
            @RequestParam("end") LocalDate end);

    @GetMapping("/admin/report/top10")
    Result<Map<String, Object>> getTop10(
            @RequestParam("begin") LocalDate begin,
            @RequestParam("end") LocalDate end);
}
