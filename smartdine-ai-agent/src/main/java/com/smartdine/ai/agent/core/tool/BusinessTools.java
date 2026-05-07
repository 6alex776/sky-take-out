package com.smartdine.ai.agent.core.tool;

import com.smartdine.ai.agent.client.ReportServiceClient;
import com.smartdine.ai.agent.client.WorkspaceServiceClient;
import com.smartdine.result.Result;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

/**
 * 经营顾问Agent工具类
 * 提供营业额、订单、用户等数据查询能力
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessTools {

    private final ReportServiceClient reportServiceClient;
    private final WorkspaceServiceClient workspaceServiceClient;

    /**
     * 查询今日营业数据
     */
    @Tool("获取今日营业数据，包括营业额、订单数、有效订单数等")
    public String getTodayBusinessData() {
        try {
            Result<Map<String, Object>> result = workspaceServiceClient.getBusinessData();
            if (result != null && result.getData() != null) {
                return formatBusinessData(result.getData());
            }
            return "暂无今日营业数据";
        } catch (Exception e) {
            log.error("获取今日营业数据失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    /**
     * 查询营业额统计
     */
    @Tool("查询指定日期范围内的营业额统计，参数格式：yyyy-MM-dd")
    public String getTurnoverStatistics(String begin, String end) {
        try {
            LocalDate beginDate = LocalDate.parse(begin);
            LocalDate endDate = LocalDate.parse(end);
            Result<Map<String, Object>> result = reportServiceClient.getTurnoverStatistics(beginDate, endDate);
            if (result != null && result.getData() != null) {
                return "营业额统计: " + result.getData().toString();
            }
            return "暂无营业额数据";
        } catch (Exception e) {
            log.error("获取营业额统计失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    /**
     * 查询用户统计
     */
    @Tool("查询指定日期范围内的用户统计，包括新增用户、总用户等")
    public String getUserStatistics(String begin, String end) {
        try {
            LocalDate beginDate = LocalDate.parse(begin);
            LocalDate endDate = LocalDate.parse(end);
            Result<Map<String, Object>> result = reportServiceClient.getUserStatistics(beginDate, endDate);
            if (result != null && result.getData() != null) {
                return "用户统计: " + result.getData().toString();
            }
            return "暂无用户统计数据";
        } catch (Exception e) {
            log.error("获取用户统计失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    /**
     * 查询订单统计
     */
    @Tool("查询指定日期范围内的订单统计")
    public String getOrderStatistics(String begin, String end) {
        try {
            LocalDate beginDate = LocalDate.parse(begin);
            LocalDate endDate = LocalDate.parse(end);
            Result<Map<String, Object>> result = reportServiceClient.getOrderReport(beginDate, endDate);
            if (result != null && result.getData() != null) {
                return "订单统计: " + result.getData().toString();
            }
            return "暂无订单统计数据";
        } catch (Exception e) {
            log.error("获取订单统计失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    /**
     * 查询销量TOP10
     */
    @Tool("查询指定日期范围内的销量排名前10的菜品")
    public String getTop10Sales(String begin, String end) {
        try {
            LocalDate beginDate = LocalDate.parse(begin);
            LocalDate endDate = LocalDate.parse(end);
            Result<Map<String, Object>> result = reportServiceClient.getTop10(beginDate, endDate);
            if (result != null && result.getData() != null) {
                return "销量TOP10: " + result.getData().toString();
            }
            return "暂无销量数据";
        } catch (Exception e) {
            log.error("获取销量TOP10失败", e);
            return "获取数据失败: " + e.getMessage();
        }
    }

    /**
     * 格式化营业数据
     */
    private String formatBusinessData(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("【今日营业概览】\n");
        sb.append("- 营业额: ").append(data.getOrDefault("turnover", "N/A")).append("\n");
        sb.append("- 有效订单数: ").append(data.getOrDefault("validOrderCount", "N/A")).append("\n");
        sb.append("- 订单完成率: ").append(data.getOrDefault("orderCompletionRate", "N/A")).append("\n");
        sb.append("- 平均客单价: ").append(data.getOrDefault("unitPrice", "N/A")).append("\n");
        sb.append("- 新增用户: ").append(data.getOrDefault("newUsers", "N/A")).append("\n");
        return sb.toString();
    }
}
