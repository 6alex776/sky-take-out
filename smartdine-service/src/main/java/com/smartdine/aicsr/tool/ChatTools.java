package com.smartdine.aicsr.tool;

import com.smartdine.result.Result;
import com.smartdine.service.ReportService;
import com.smartdine.service.WorkspaceService;
import com.smartdine.vo.*;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AI智能客服工具类
 * 提供AI可调用的一系列工具方法，用于获取平台经营数据
 */
@Slf4j
@Component
public class ChatTools {

    @Autowired
    private ReportService reportService;

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 获取指定日期范围的营业额统计
     *
     * @param beginDate 开始日期，格式：yyyy-MM-dd
     * @param endDate   结束日期，格式：yyyy-MM-dd
     * @return 营业额统计数据，包含日期列表和对应营业额
     */
    @Tool(name = "getTurnoverStatistics", value = "获取指定日期范围的营业额统计数据，用于分析营业趋势")
    public TurnoverReportVO getTurnoverStatistics(String beginDate, String endDate) {
        try {
            log.info("AI调用工具：获取营业额统计，时间范围：{} 至 {}", beginDate, endDate);
            LocalDate begin = LocalDate.parse(beginDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
            return reportService.turnover(begin, end);
        } catch (Exception e) {
            log.error("获取营业额统计失败", e);
            return null;
        }
    }

    /**
     * 获取指定日期范围的订单统计
     *
     * @param beginDate 开始日期，格式：yyyy-MM-dd
     * @param endDate   结束日期，格式：yyyy-MM-dd
     * @return 订单统计数据，包含日期列表和对应订单数量
     */
    @Tool(name = "getOrderStatistics", value = "获取指定日期范围的订单统计数据，用于分析订单趋势")
    public OrderReportVO getOrderStatistics(String beginDate, String endDate) {
        try {
            log.info("AI调用工具：获取订单统计，时间范围：{} 至 {}", beginDate, endDate);
            LocalDate begin = LocalDate.parse(beginDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
            return reportService.order(begin, end);
        } catch (Exception e) {
            log.error("获取订单统计失败", e);
            return null;
        }
    }

    /**
     * 获取指定日期范围的用户统计
     *
     * @param beginDate 开始日期，格式：yyyy-MM-dd
     * @param endDate   结束日期，格式：yyyy-MM-dd
     * @return 用户统计数据，包含新增用户和总用户数
     */
    @Tool(name = "getUserStatistics", value = "获取指定日期范围的用户统计数据，用于分析用户增长趋势")
    public UserReportVO getUserStatistics(String beginDate, String endDate) {
        try {
            log.info("AI调用工具：获取用户统计，时间范围：{} 至 {}", beginDate, endDate);
            LocalDate begin = LocalDate.parse(beginDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
            return reportService.user(begin, end);
        } catch (Exception e) {
            log.error("获取用户统计失败", e);
            return null;
        }
    }

    /**
     * 获取指定日期范围的销量排名TOP10
     *
     * @param beginDate 开始日期，格式：yyyy-MM-dd
     * @param endDate   结束日期，格式：yyyy-MM-dd
     * @return 销量排名数据，包含菜品名称和销量
     */
    @Tool(name = "getTop10Sales", value = "获取指定日期范围的销量排名TOP10，用于分析热销菜品")
    public SalesTop10ReportVO getTop10Sales(String beginDate, String endDate) {
        try {
            log.info("AI调用工具：获取销量排名，时间范围：{} 至 {}", beginDate, endDate);
            LocalDate begin = LocalDate.parse(beginDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
            return reportService.top(begin, end);
        } catch (Exception e) {
            log.error("获取销量排名失败", e);
            return null;
        }
    }

    /**
     * 获取工作台今日数据概览
     * 包含订单数、营业额、待处理订单等关键指标
     *
     * @return 今日工作台数据
     */
    @Tool(name = "getTodayWorkspaceData", value = "获取今日工作台数据概览，包含订单数、营业额、待处理订单等关键指标")
    public BusinessDataVO getTodayWorkspaceData() {
        try {
            log.info("AI调用工具：获取今日工作台数据");
            LocalDateTime begin = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime end = LocalDateTime.now();
            return workspaceService.getBusinessData(begin, end);
        } catch (Exception e) {
            log.error("获取工作台数据失败", e);
            return null;
        }
    }

    /**
     * 获取今日订单概览数据
     * 包含待接单、待派送、已完成、已取消等订单状态统计
     *
     * @return 今日订单概览
     */
    @Tool(name = "getTodayOrderOverview", value = "获取今日订单概览，包含待接单、待派送、已完成、已取消等订单状态统计")
    public OrderOverViewVO getTodayOrderOverview() {
        try {
            log.info("AI调用工具：获取今日订单概览");
            return workspaceService.getOrderOverView();
        } catch (Exception e) {
            log.error("获取订单概览失败", e);
            return null;
        }
    }

    /**
     * 获取菜品总览数据
     * 包含已启售、已停售菜品数量
     *
     * @return 菜品总览数据
     */
    @Tool(name = "getDishOverview", value = "获取菜品总览数据，包含已启售和已停售菜品数量")
    public DishOverViewVO getDishOverview() {
        try {
            log.info("AI调用工具：获取菜品总览");
            return workspaceService.getDishOverView();
        } catch (Exception e) {
            log.error("获取菜品总览失败", e);
            return null;
        }
    }

    /**
     * 获取套餐总览数据
     * 包含已启售、已停售套餐数量
     *
     * @return 套餐总览数据
     */
    @Tool(name = "getSetmealOverview", value = "获取套餐总览数据，包含已启售和已停售套餐数量")
    public SetmealOverViewVO getSetmealOverview() {
        try {
            log.info("AI调用工具：获取套餐总览");
            return workspaceService.getSetmealOverView();
        } catch (Exception e) {
            log.error("获取套餐总览失败", e);
            return null;
        }
    }

    /**
     * 获取当前日期时间
     * 用于AI确认当前时间
     *
     * @return 当前日期时间字符串
     */
    @Tool(name = "getCurrentDateTime", value = "获取当前日期时间，用于确认当前时间")
    public String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取今日日期
     * 用于AI确认今天的日期
     *
     * @return 今日日期字符串，格式：yyyy-MM-dd
     */
    @Tool(name = "getTodayDate", value = "获取今日日期，格式为yyyy-MM-dd")
    public String getTodayDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 获取本周日期范围
     * 用于AI获取本周的起止日期
     *
     * @return 本周日期范围，格式：beginDate,endDate
     */
    @Tool(name = "getWeekDateRange", value = "获取本周日期范围（周一到周日），格式为yyyy-MM-dd")
    public String getWeekDateRange() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate sunday = monday.plusDays(6);
        return monday.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + sunday.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 获取本月日期范围
     * 用于AI获取本月的起止日期
     *
     * @return 本月日期范围，格式：beginDate,endDate
     */
    @Tool(name = "getMonthDateRange", value = "获取本月日期范围，格式为yyyy-MM-dd")
    public String getMonthDateRange() {
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());
        return firstDay.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + lastDay.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
