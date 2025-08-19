package com.sky.aicsr.tool;

import com.sky.aicsr.ChatService;
import com.sky.service.WorkspaceService;
import com.sky.service.impl.WorkspaceServiceImpl;
import com.sky.vo.BusinessDataVO;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("chatTools")
public class ChatTools {

//    @Autowired
//    private ChatService chatService;
    @Autowired
    private WorkspaceService workspaceService;

    @Tool(name = "今日数据",value = "根据参数，执行getTodayDate方法查询今日的数据，并根据用户的提问返回对应真实的结果，若找不到结果则回答“无结果”")
    public String getTodayData(LocalDateTime begin, LocalDateTime end) {
        // 调用 WorkspaceService 的 getBusinessData 方法获取今日营业数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);

        // 格式化返回结果
        StringBuilder result = new StringBuilder();
        result.append("今日营业数据:\n");
        result.append("营业额: ").append(businessDataVO.getTurnover()).append("元\n");
        result.append("有效订单数: ").append(businessDataVO.getValidOrderCount()).append("单\n");
        result.append("订单完成率: ").append(String.format("%.2f", businessDataVO.getOrderCompletionRate() * 100)).append("%\n");
        result.append("平均客单价: ").append(String.format("%.2f", businessDataVO.getUnitPrice())).append("元\n");
        result.append("新增用户数: ").append(businessDataVO.getNewUsers()).append("人");

        return result.toString();
    }

}
