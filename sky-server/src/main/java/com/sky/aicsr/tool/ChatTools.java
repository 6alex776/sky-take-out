package com.sky.aicsr.tool;

import com.sky.aicsr.ChatService;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.service.impl.WorkspaceServiceImpl;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.SalesTop10ReportVO;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component("chatTools")
public class ChatTools {

    //    @Autowired
//    private ChatService chatService;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private ReportService reportService;

    @Tool(name = "数据分析", value = "根据参数，执行getTodayDate方法查询对应时间数据，并根据用户的提问返回对应真实的结果，若找不到结果则回答“无结果”")
    public String getTodayData(LocalDateTime begin, LocalDateTime end) {
        // 调用 WorkspaceService 的 getBusinessData 方法获取今日营业数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);

        // 格式化返回结果
        StringBuilder result = new StringBuilder();
        result.append("数据:\n");
        result.append("营业额: ").append(businessDataVO.getTurnover()).append("元\n");
        result.append("有效订单数: ").append(businessDataVO.getValidOrderCount()).append("单\n");
        result.append("订单完成率: ").append(String.format("%.2f", businessDataVO.getOrderCompletionRate() * 100)).append("%\n");
        result.append("平均客单价: ").append(String.format("%.2f", businessDataVO.getUnitPrice())).append("元\n");
        result.append("新增用户数: ").append(businessDataVO.getNewUsers()).append("人");

        return result.toString();
    }

    @Tool(name = "获取销量排名", value = "根据参数，执行getSalesRanking方法查询对应时间数据，并根据用户的提问返回对应真实的结果，若找不到结果则回答“无结果”")
    public String getSalesRanking(LocalDate begin, LocalDate end) {
        try { // 调用 WorkspaceService 的 getBusinessData 方法获取今日营业数据
            SalesTop10ReportVO salesTop10ReportVO = reportService.top(begin, end);


            // 检查是否有数据
            if (salesTop10ReportVO == null ||
                    (salesTop10ReportVO.getNameList() == null || salesTop10ReportVO.getNameList().isEmpty()) &&
                            (salesTop10ReportVO.getNumberList() == null || salesTop10ReportVO.getNumberList().isEmpty())) {
                return "无结果";
            }

            // 解析数据
            String[] names = salesTop10ReportVO.getNameList().split(",");
            String[] numbers = salesTop10ReportVO.getNumberList().split(",");

            // 构建返回结果
            StringBuilder result = new StringBuilder();
            result.append("## 销量排名 TOP 10\n\n");
            result.append("**统计时间范围:** ").append(begin.toString())
                    .append(" 至 ").append(end.toString()).append("\n\n");
            result.append("| 排名 | 菜品名称 | 销量 |\n");
            result.append("|------|----------|------|\n");

            // 添加排名数据
            for (int i = 0; i < Math.min(names.length, numbers.length); i++) {
                result.append("| ").append(i + 1)
                        .append(" | ").append(names[i])
                        .append(" | ").append(numbers[i])
                        .append(" |\n");
            }
            return result.toString();
        } catch (
                Exception e) {
            return "销量排名查询过程中发生错误: " + e.getMessage();
        }
    }
}